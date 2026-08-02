package com.isaacshub.app.routehelper.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors

/**
 * Enhanced Amazon Route Scanner Screen V2
 * Features:
 * - Real-time AR-style address highlighting with bounding boxes
 * - Green boxes for matched addresses, red for unmatched
 * - Tap on highlighted address to add as next stop
 * - Integrated +/- package count buttons in the stop list
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmazonRouteScannerScreenV2(
    routeId: Long,
    onComplete: () -> Unit
) {
    val viewModel: AmazonRouteScannerViewModelV2 = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(routeId) {
        viewModel.start(routeId)
    }

    // Show validation messages as snackbar
    LaunchedEffect(uiState.validationMessage) {
        uiState.validationMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearValidationMessage()
        }
    }

    // Show errors as snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Scan Addresses (${uiState.scannedStops.size})") },
                navigationIcon = {
                    IconButton(onClick = onComplete) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.scannedStops.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { viewModel.finishScanning(onComplete) }
                ) {
                    if (uiState.isCreatingStops) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Icon(Icons.Filled.Check, contentDescription = "Finish & Create Route")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Camera preview with AR overlay (top 50% of screen)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    AddressCameraScannerWithOverlay(
                        candidateAddresses = uiState.candidateAddresses,
                        onAddressSelected = { address ->
                            viewModel.addStopFromAddress(address)
                        }
                    )

                    // Instructions overlay
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.TopCenter),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Tap GREEN addresses to add stops",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Green = Matched | Red = Not in ZIP",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Scanned stops list with package count controls (bottom 50% of screen)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(
                            "Scanned Stops",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        if (uiState.scannedStops.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No stops added yet",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(uiState.scannedStops) { stop ->
                                    StopListItemWithPackageCounter(
                                        stop = stop,
                                        onIncrement = { viewModel.incrementPackageCount(stop) },
                                        onDecrement = { viewModel.decrementPackageCount(stop) },
                                        onDelete = { viewModel.removeStop(stop) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * List item for a scanned stop with integrated +/- package count buttons
 */
@Composable
private fun StopListItemWithPackageCounter(
    stop: ScannedStopV2,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stop info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${stop.sequenceNumber}. ${stop.addressLabel}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            // Package counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onDecrement,
                    enabled = stop.packageCount > 0
                ) {
                    Icon(
                        Icons.Filled.Remove,
                        contentDescription = "Decrease packages",
                        tint = if (stop.packageCount > 0) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        }
                    )
                }

                Text(
                    text = "${stop.packageCount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(32.dp)
                )

                IconButton(onClick = onIncrement) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Increase packages",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete stop",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Camera scanner with AR-style address overlay showing bounding boxes
 */
@Composable
private fun AddressCameraScannerWithOverlay(
    candidateAddresses: List<com.isaacshub.app.routehelper.data.CandidateAddressEntity>,
    onAddressSelected: (DetectedAddress) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // State for detected addresses with bounding boxes
    var detectedAddresses by remember { mutableStateOf<List<DetectedAddress>>(emptyList()) }
    var previewSize by remember { mutableStateOf(Size.Zero) }
    var imageSize by remember { mutableStateOf(Size.Zero) }
    var imageRotation by remember { mutableStateOf(0) }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Camera preview
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysis ->
                                analysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                                    processImageForAddresses(
                                        imageProxy,
                                        recognizer,
                                        candidateAddresses
                                    ) { addresses, imgSize, rotation ->
                                        detectedAddresses = addresses
                                        imageSize = imgSize
                                        imageRotation = rotation
                                    }
                                }
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            android.util.Log.e("CameraScanner", "Failed to bind camera", e)
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(detectedAddresses) {
                        detectTapGestures { tapOffset ->
                            // Find which address was tapped
                            detectedAddresses.firstOrNull { detected ->
                                detected.boundingBox.contains(tapOffset)
                            }?.let { tappedAddress ->
                                if (tappedAddress.isMatched) {
                                    onAddressSelected(tappedAddress)
                                }
                            }
                        }
                    }
                    .onSizeChanged { size ->
                        previewSize = Size(
                            size.width.toFloat(),
                            size.height.toFloat()
                        )
                    }
            )

            // Draw bounding boxes over camera preview
            if (previewSize != Size.Zero && imageSize != Size.Zero) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    detectedAddresses.forEach { detected ->
                        // Transform coordinates from image space to preview space
                        val transformedBox = transformBoundingBox(
                            detected.imageBoundingBox,
                            imageSize,
                            previewSize
                        )

                        // Draw bounding box
                        val boxColor = if (detected.isMatched) {
                            Color.Green
                        } else {
                            Color.Red
                        }

                        drawRect(
                            color = boxColor,
                            topLeft = transformedBox.topLeft,
                            size = transformedBox.size,
                            style = Stroke(width = 4.dp.toPx())
                        )

                        // Draw filled background for text
                        val labelHeight = 30.dp.toPx()
                        drawRect(
                            color = boxColor.copy(alpha = 0.7f),
                            topLeft = Offset(
                                transformedBox.left,
                                transformedBox.top - labelHeight
                            ),
                            size = Size(transformedBox.width, labelHeight)
                        )
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission required")
        }
    }
}

/**
 * Process camera frame to detect addresses and create bounding boxes
 */
private fun processImageForAddresses(
    imageProxy: ImageProxy,
    recognizer: com.google.mlkit.vision.text.TextRecognizer,
    candidateAddresses: List<com.isaacshub.app.routehelper.data.CandidateAddressEntity>,
    onAddressesDetected: (List<DetectedAddress>, Size, Int) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedAddresses = mutableListOf<DetectedAddress>()
                val imgSize = Size(image.width.toFloat(), image.height.toFloat())
                val rotation = imageProxy.imageInfo.rotationDegrees

                // Process each text block
                for (block in visionText.textBlocks) {
                    for (line in block.lines) {
                        val text = line.text

                        // Check if this looks like an address
                        if (com.isaacshub.app.routehelper.ui.scanner.isValidStreetAddress(text)) {
                            // Try to match with candidate addresses
                            val matchedCandidate = com.isaacshub.app.routehelper.util.findBestCandidateMatch(
                                text,
                                candidateAddresses,
                                similarityThreshold = 0.6
                            )

                            val isMatched = matchedCandidate != null
                            val boundingBox = line.boundingBox

                            if (boundingBox != null) {
                                detectedAddresses.add(
                                    DetectedAddress(
                                        text = text,
                                        isMatched = isMatched,
                                        matchedCandidateId = matchedCandidate?.id,
                                        matchedLabel = matchedCandidate?.label,
                                        imageBoundingBox = boundingBox,
                                        boundingBox = Rect.Zero // Will be transformed later
                                    )
                                )
                            }
                        }
                    }
                }

                onAddressesDetected(detectedAddresses, imgSize, rotation)
            }
            .addOnFailureListener { e ->
                android.util.Log.e("AddressScanner", "Text recognition failed", e)
                onAddressesDetected(emptyList(), Size.Zero, 0)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
        onAddressesDetected(emptyList(), Size.Zero, 0)
    }
}

/**
 * Transform bounding box from image coordinates to preview coordinates
 */
private fun transformBoundingBox(
    imageBox: android.graphics.Rect,
    imageSize: Size,
    previewSize: Size
): Rect {
    // Calculate scale factors
    val scaleX = previewSize.width / imageSize.width
    val scaleY = previewSize.height / imageSize.height

    return Rect(
        left = imageBox.left * scaleX,
        top = imageBox.top * scaleY,
        right = imageBox.right * scaleX,
        bottom = imageBox.bottom * scaleY
    )
}

/**
 * Data class for a detected address with bounding box
 */
data class DetectedAddress(
    val text: String,
    val isMatched: Boolean,
    val matchedCandidateId: Long?,
    val matchedLabel: String?,
    val imageBoundingBox: android.graphics.Rect,
    val boundingBox: Rect  // Transformed to preview coordinates
)
