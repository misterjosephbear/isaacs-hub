# Graph Report - isaacs-hub  (2026-08-06)

## Corpus Check
- 341 files · ~145,561 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 2802 nodes · 4381 edges · 225 communities (161 shown, 64 thin omitted)
- Extraction: 93% EXTRACTED · 7% INFERRED · 0% AMBIGUOUS · INFERRED: 318 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `c9bc3609`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- AddressInterpolationTest
- EssentialsApi
- RoutePlayerViewModel
- EssentialsRepository
- SleepDetectionEngine
- .httpRequest
- PackageScanScreen.kt
- CandidateAddress
- AmazonRouteScannerViewModel
- EditRouteViewModel
- MailScanViewModel
- RoutePlayerService
- CreateChoreViewModel
- RouteHelperRepository
- RouteHelperDao
- LocationSample
- HomeScreen
- BankingDao
- BudgetConfigViewModel
- RouteScheduleOverrideEntity
- TimeEntryEntity
- HomeViewModel
- BaseApiClient
- RoutedStopEntity
- AppBlockingService
- EditRuleScreen
- .prefs
- IndianaAddressPointFetcher
- RouteEntity
- ActivityMapperModels.kt
- ActivityMapperRepository
- PlaidConnectionSetupViewModel
- newOsmMapView
- TimeTrackerSettingsViewModel
- ChildAccountEntity
- FeaturePromptEntity
- NapAlarmService
- EditTimeEntryViewModel
- AmazonRouteScannerViewModelV2
- UserPreferencesRepository
- FeatureFunnelHomeViewModel
- Routes
- computeWeeklySummary
- formatNumber
- PhotoCaptureViewModel
- CompletionStatus
- EssentialsRepository
- FeatureFunnelPreferencesRepository
- RouteHelperHomeViewModel
- VaultApiClient
- AuthRepository
- ActivityMapperViewModel
- DeductionType
- CrashLogDao
- WorkDatabase
- BankingHomeViewModel
- FeatureFunnelRepository
- EditPromptViewModel
- BankingRepository
- ChoreCompletionEntity
- LandingViewModel
- RouteHelperEntities.kt
- SettingsViewModel
- WeekScheduleViewModel
- TigerShapefileParserTest
- ChoreEntity
- ManageFamilyViewModel
- SleepEntry
- EditSessionViewModel
- NapViewModel
- RoutePlayerScreen.kt
- VaultHomeViewModel
- api/EssentialsApiClient.kt
- ChoreDetailViewModel
- LoginViewModel
- CrashLogger
- SleepSessionEntity
- BankAccount
- CLAUDE.md
- PromptStatus
- AppDatabase
- TigerShapefileParser.kt
- FeatureFunnelApiClient
- SleepRepository
- TimeTrackingDatabase
- VaultPreferencesRepository
- .onCreate
- Comprehensive App Data Backup System - Implementation Summary
- TimeTrackingHomeViewModel
- GeoPoint
- ChargingMonitorReceiver
- Routes
- DeviceAdminManager
- Transaction
- HomeControlApiClient
- .put
- EssentialsDatabase
- EssentialsAdminViewModel
- RouteHelperRouteEntity
- HomeControlRepository
- TigerAddressFetcher
- HistoryViewModel
- HomeViewModel
- computeWeekSchedule
- UpdateViewModel
- UpdateViewModel
- ActivityMapperHomeScreen
- DeviceDetailScreen
- RouteHelperDatabase.kt
- SleepSource
- computeDebt
- NapAlarmScheduler
- EssentialsRepository
- FeatureFunnelScheduler
- .fetchLatestRelease
- VaultHomeScreen
- EssentialsDeviceAdminReceiver
- SetupScreen
- .fetchLatestRelease
- ChoreEntity.kt
- RouteSectionEntity
- TriggerType
- SleepNotifications
- NapNotifications
- .downloadAndInstall
- PolylineOffset.kt
- AppDataBackupScheduler
- EssentialsNavGraph
- .downloadAndInstall
- EnergyForecastCalculatorTest
- WindDownCalculatorTest
- filterAddressGroupsNearBuildings
- DatabaseMigrationHelper
- Context
- LandingPreferencesRepository
- AddressMatching.kt
- BootCompletedReceiver
- NapAlarmReceiver
- PhotoBackupScheduler
- Exception
- StopSideTest
- RouteEditViewModel
- DebugLogger
- ConditionOperator
- CountyFipsLookup
- PackageCleanupScheduler
- PackageCleanupWorker
- SleepDetectionController
- .getAccounts
- WindDownCalculator
- ChoreCard
- EditSessionScreen
- NapScreen
- sleep/ui/settings/SettingsScreen.kt
- UpdateBanner
- gradlew
- HomeControlViewModel
- HistoryScreen
- CoroutineWorker
- PlaidConfig.kt
- deploy-apk.sh
- App
- BaseApiClient
- stickyNearestSlots
- androidx
- Result
- BankingDatabase
- Result
- .findOptimalInsertionPoint
- Room
- HomeControlModels.kt
- JSONObject
- com
- NumberFormat
- Context
- RoomDatabase
- Modifier
- NavHostController
- Capability
- Routine
- RoutineBuilderScreen
- Application
- ScannedAddressData
- AndroidViewModel
- RoomDatabase
- StateFlow
- ImageProxy
- DeviceType
- IsaacsHubScaffold
- TextRecognizer
- Uri
- PayType
- .checkForCompletion
- ViewModel
- RoutineCard
- parseScannedAddresses
- VaultPreferencesRepository
- Application
- MultiUrlApiClient
- RoomDatabase
- Flow
- CoroutineWorker
- LandingScreen.kt
- PromptCard
- androidx
- MailScanScreen
- T
- ViewModel
- ViewModelProvider
- PhotoCaptureScreen
- ByteArray
- RouteHelperDao
- SupportSQLiteDatabase

## God Nodes (most connected - your core abstractions)
1. `GeoPoint` - 68 edges
2. `HomeControlRepository` - 45 edges
3. `RouteHelperRepository` - 44 edges
4. `RouteHelperDao` - 39 edges
5. `HomeControlApiClient` - 38 edges
6. `RoutedStopEntity` - 30 edges
7. `RoutePlayerViewModel` - 29 edges
8. `ActivityMapperApiClient` - 24 edges
9. `FeaturePromptEntity` - 23 edges
10. `BankingRepository` - 23 edges

## Surprising Connections (you probably didn't know these)
- `toSleepEntry()` --references--> `SleepEntry`  [EXTRACTED]
  app/src/main/java/com/isaacshub/app/sleep/data/SleepRepository.kt → sleepcore/src/main/kotlin/com/isaacshub/sleep/core/SleepDebt.kt
- `RoutePlayerScreen()` --calls--> `ScannedPackage`  [INFERRED]
  app/src/main/java/com/isaacshub/app/routehelper/ui/player/RoutePlayerScreen.kt → app/src/main/java/com/isaacshub/app/routehelper/ui/player/PackageScanScreen.kt
- `RoutePlayerScreen()` --calls--> `PackageScanScreen()`  [INFERRED]
  app/src/main/java/com/isaacshub/app/routehelper/ui/player/RoutePlayerScreen.kt → app/src/main/java/com/isaacshub/app/routehelper/ui/player/PackageScanScreen.kt
- `RoutePlayerScreen()` --calls--> `PlotUnknownPackageDialog()`  [INFERRED]
  app/src/main/java/com/isaacshub/app/routehelper/ui/player/RoutePlayerScreen.kt → app/src/main/java/com/isaacshub/app/routehelper/ui/player/PackageScanScreen.kt
- `DeviceDetailScreen()` --calls--> `HomeControlApiClient`  [INFERRED]
  app/src/main/java/com/isaacshub/app/homecontrol/ui/devices/DeviceDetailScreen.kt → app/src/main/java/com/isaacshub/app/homecontrol/data/HomeControlApiClient.kt

## Import Cycles
- None detected.

## Communities (225 total, 64 thin omitted)

### Community 0 - "AddressInterpolationTest"
Cohesion: 0.15
Nodes (15): AddressParity, BOTH, EVEN, ODD, UNKNOWN, HouseNumberRange, interpolateAddresses(), interpolateAddressGroups() (+7 more)

### Community 1 - "EssentialsApi"
Cohesion: 0.05
Nodes (31): EssentialsApi, EssentialsEndpointPaths, Result, Chore, CreateChoreRequest, UpdateChoreRequest, AdminOverrideRequest, ChoreCompletion (+23 more)

### Community 2 - "RoutePlayerViewModel"
Cohesion: 0.08
Nodes (10): AndroidViewModel, com, Flow, GeoPoint, StateFlow, RoadRouteResult, RoutePlayerUiState, RoutePlayerViewModel (+2 more)

### Community 3 - "EssentialsRepository"
Cohesion: 0.05
Nodes (22): ChoreDao, Flow, CompletionDao, Flow, DayOfWeekListConverter, LocalChoreEntity, CompletionStatus, COMPLETED (+14 more)

### Community 4 - "SleepDetectionEngine"
Cohesion: 0.08
Nodes (23): IBinder, Intent, Service, SharedPreferences, SleepDetectionService, SensorManager, DetectionConfig, DetectionEvent (+15 more)

### Community 5 - ".httpRequest"
Cohesion: 0.13
Nodes (14): ApiException, ChildAccountDto, ChoreDto, EssentialsApiClient, JSONObject, Result, T, NominatimGeocoder (+6 more)

### Community 6 - "PackageScanScreen.kt"
Cohesion: 0.24
Nodes (17): checkUnknownStreetMatch(), extractAddressWithStop(), extractBestAddressCandidate(), extractStreetName(), extractTrackingFromBarcodes(), extractUSPSTrackingNumber(), isUSPSTracking(), com (+9 more)

### Community 7 - "CandidateAddress"
Cohesion: 0.14
Nodes (9): distanceMeters(), CandidateAddress, nearestAddresses(), AndroidViewModel, StateFlow, RouteHelperTestUiState, RouteHelperTestViewModel, TestStop (+1 more)

### Community 8 - "AmazonRouteScannerViewModel"
Cohesion: 0.11
Nodes (18): extractAddress(), ExtractedAddress, extractFromDirectionSheet(), isValidStreetAddress(), AddressCameraScanner(), AddressListItem(), AmazonRouteScannerScreen(), com (+10 more)

### Community 9 - "EditRouteViewModel"
Cohesion: 0.08
Nodes (18): ScheduleType, BIWEEKLY, DAILY_MON_FRI, DAILY_SAT_FRI, NONE, WEEKLY, ScheduleTypeConverter, AnchorDatePickerDialog() (+10 more)

### Community 10 - "MailScanViewModel"
Cohesion: 0.19
Nodes (6): AndroidViewModel, ScannedAddress, StateFlow, MailScanUiState, MailScanViewModel, ResolvedMailStop

### Community 11 - "RoutePlayerService"
Cohesion: 0.11
Nodes (14): View, OverlayTouchListener, IBinder, Intent, Notification, Service, View, WindowManager (+6 more)

### Community 12 - "CreateChoreViewModel"
Cohesion: 0.09
Nodes (11): CreateChoreUiState, CreateChoreViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider, DownloadManager (+3 more)

### Community 13 - "RouteHelperRepository"
Cohesion: 0.12
Nodes (3): PackageEntity, Flow, RouteHelperRepository

### Community 14 - "RouteHelperDao"
Cohesion: 0.11
Nodes (3): Flow, RouteHelperDao, CandidateAddressEntity

### Community 15 - "LocationSample"
Cohesion: 0.06
Nodes (21): LocationSample, advanceToNextStop(), resolveMapBearing(), StopSide, IN_DRIVE, LEFT, NONE, RIGHT (+13 more)

### Community 16 - "HomeScreen"
Cohesion: 0.12
Nodes (16): EnergyForecastChart(), formatHourOfDay(), Modifier, Modifier, SleepDebtRing(), formatDuration(), formatHourOfDay(), formatRange() (+8 more)

### Community 17 - "BankingDao"
Cohesion: 0.05
Nodes (24): BankAccountDao, Flow, BankAccountEntity, fromDomain(), BankConnectionDao, Flow, BankConnectionEntity, fromDomain() (+16 more)

### Community 18 - "BudgetConfigViewModel"
Cohesion: 0.05
Nodes (26): BudgetAccountSelectionEntity, BudgetCategoryEntity, BudgetDao, Flow, BudgetCategory, toDomain(), toEntity(), BudgetState (+18 more)

### Community 19 - "RouteScheduleOverrideEntity"
Cohesion: 0.11
Nodes (10): Flow, RouteScheduleOverrideDao, RouteScheduleOverrideEntity, currentPayPeriodRange(), ClosedRange, computePayPeriodSummary(), PayPeriodSummary, ClosedRange (+2 more)

### Community 20 - "TimeEntryEntity"
Cohesion: 0.13
Nodes (5): Flow, TimeEntryDao, TimeEntryEntity, Flow, TimeTrackingRepository

### Community 21 - "HomeViewModel"
Cohesion: 0.12
Nodes (13): ChoreCard(), CompletionProgressCard(), HomeScreen(), Modifier, ChoreUiState, Factory, HomeUiState, HomeViewModel (+5 more)

### Community 22 - "BaseApiClient"
Cohesion: 0.18
Nodes (9): BaseApiClient, HttpURLConnection, JSONObject, MultiUrlApiClient, AlreadyRunning, DebugTriggerResult, Failed, SettingsApiClient (+1 more)

### Community 23 - "RoutedStopEntity"
Cohesion: 0.14
Nodes (8): RoutedStopEntity, GeoPoint, AddSectionDialog(), RouteEditScreen(), SectionRow(), StopRow(), AmazonLoadTruckScreen(), LoadTruckStopItem()

### Community 24 - "AppBlockingService"
Cohesion: 0.16
Nodes (10): AppBlockingService, Context, IBinder, Intent, Notification, Service, View, WindowManager (+2 more)

### Community 25 - "EditRuleScreen"
Cohesion: 0.70
Nodes (4): Condition, ActionEditor(), ConditionEditor(), EditRuleScreen()

### Community 26 - ".prefs"
Cohesion: 0.16
Nodes (10): Context, NapAlarmController, Context, SharedPreferences, NapPhase, IDLE, NAPPING, RINGING (+2 more)

### Community 27 - "IndianaAddressPointFetcher"
Cohesion: 0.13
Nodes (8): AddressFetchResult, Failure, FetchedAddress, Success, IndianaAddressPointFetcher, JSONObject, RouteHelperAddressFetcher, IndianaAddressPointFetcherTest

### Community 28 - "RouteEntity"
Cohesion: 0.15
Nodes (9): Flow, RouteDao, RouteEntity, Factory, StateFlow, T, ViewModel, ViewModelProvider (+1 more)

### Community 29 - "ActivityMapperModels.kt"
Cohesion: 0.09
Nodes (23): Action, ActionType, SET_DISCORD_RICH_PRESENCE, ActivityMapperData, ComparisonOperator, EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL (+15 more)

### Community 30 - "ActivityMapperRepository"
Cohesion: 0.27
Nodes (3): ActivityMapperRepository, Result, StateFlow

### Community 31 - "PlaidConnectionSetupViewModel"
Cohesion: 0.13
Nodes (13): CreatingLinkToken, Error, ExchangingToken, Factory, Idle, StateFlow, T, ViewModel (+5 more)

### Community 32 - "newOsmMapView"
Cohesion: 0.17
Nodes (11): Modifier, LiveMap(), RouteBuilderScreen(), AddressActionCard(), SideButton(), Context, newOsmMapView(), Modifier (+3 more)

### Community 33 - "TimeTrackerSettingsViewModel"
Cohesion: 0.09
Nodes (11): DeductionDao, Flow, DeductionEntity, DeductionFormState, Factory, StateFlow, T, ViewModel (+3 more)

### Community 34 - "ChildAccountEntity"
Cohesion: 0.18
Nodes (6): ChildAccountDao, Flow, ChildAccountEntity, ChildAccountCard(), ChildAccountDialog(), ManageFamilyScreen()

### Community 35 - "FeaturePromptEntity"
Cohesion: 0.29
Nodes (3): FeaturePromptDao, Flow, FeaturePromptEntity

### Community 36 - "NapAlarmService"
Cohesion: 0.20
Nodes (9): Context, IBinder, Intent, Service, NapAlarmService, start(), stop(), MediaPlayer (+1 more)

### Community 37 - "EditTimeEntryViewModel"
Cohesion: 0.13
Nodes (7): EditTimeEntryUiState, EditTimeEntryViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 38 - "AmazonRouteScannerViewModelV2"
Cohesion: 0.14
Nodes (9): AmazonRouteScannerViewModelV2, AmazonScannerV2UiState, AndroidViewModel, StateFlow, ScanMode, NON_ROUTABLES, REGULAR_STOPS, ScannedStopV2 (+1 more)

### Community 39 - "UserPreferencesRepository"
Cohesion: 0.08
Nodes (11): Keys, Flow, Preferences, UserPreferences, UserPreferencesRepository, Factory, StateFlow, T (+3 more)

### Community 40 - "FeatureFunnelHomeViewModel"
Cohesion: 0.14
Nodes (7): Factory, FeatureFunnelHomeViewModel, FeatureFunnelUiState, StateFlow, T, ViewModel, ViewModelProvider

### Community 42 - "computeWeeklySummary"
Cohesion: 0.26
Nodes (9): computeCarryoverHours(), computeWeeklySummary(), currentWeekRange(), ClosedRange, localDate(), WeeklySummary, weekStartFor(), hourlyEntry() (+1 more)

### Community 43 - "formatNumber"
Cohesion: 0.12
Nodes (16): Modifier, PayPeriodSummaryCard(), TimeEntryRow(), Modifier, WeeklyHoursBar(), EditTimeEntryScreen(), EntryDatePickerDialog(), RouteSelector() (+8 more)

### Community 44 - "PhotoCaptureViewModel"
Cohesion: 0.14
Nodes (9): Factory, Context, StateFlow, T, Uri, ViewModel, ViewModelProvider, PhotoCaptureUiState (+1 more)

### Community 45 - "CompletionStatus"
Cohesion: 0.15
Nodes (10): CompletionStatus, COMPLETED, FAILED, IN_PROGRESS, NOT_STARTED, PENDING_VERIFICATION, REJECTED, VERIFIED (+2 more)

### Community 47 - "FeatureFunnelPreferencesRepository"
Cohesion: 0.22
Nodes (5): FeatureFunnelPreferences, FeatureFunnelPreferencesRepository, Keys, Flow, Preferences

### Community 48 - "RouteHelperHomeViewModel"
Cohesion: 0.39
Nodes (4): AndroidViewModel, StateFlow, NewRouteUiState, RouteHelperHomeViewModel

### Community 49 - "VaultApiClient"
Cohesion: 0.05
Nodes (26): VaultApiClient, MediaItem, MediaType, IMAGE, VIDEO, PendingUpload, planPendingUploads(), remoteFolderFor() (+18 more)

### Community 50 - "AuthRepository"
Cohesion: 0.20
Nodes (6): AuthRepository, AuthToken, Result, EssentialsApp, Application, EssentialsRepository

### Community 51 - "ActivityMapperViewModel"
Cohesion: 0.15
Nodes (7): ActivityMapperUiState, ActivityMapperViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 52 - "DeductionType"
Cohesion: 0.32
Nodes (4): DeductionType, FLAT, PERCENT, DeductionTypeConverter

### Community 53 - "CrashLogDao"
Cohesion: 0.13
Nodes (9): CrashLog, DatabaseError, CrashLogDao, CrashLogDatabase, CrashLogDatabaseImpl, CrashLogEntity, getInstance(), Context (+1 more)

### Community 54 - "WorkDatabase"
Cohesion: 0.13
Nodes (14): columnExists(), getColumnType(), getInstance(), insertDefaultDeductions(), Context, RoomDatabase, RouteHelperDao, SupportSQLiteDatabase (+6 more)

### Community 55 - "BankingHomeViewModel"
Cohesion: 0.16
Nodes (10): BankingHomeViewModel, BankingUiState, Error, Factory, StateFlow, T, ViewModel, ViewModelProvider (+2 more)

### Community 57 - "EditPromptViewModel"
Cohesion: 0.15
Nodes (7): EditPromptUiState, EditPromptViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 58 - "BankingRepository"
Cohesion: 0.07
Nodes (14): BankingRepository, BankAccount, Flow, Result, Flow, TransactionDao, fromDomain(), TransactionEntity (+6 more)

### Community 59 - "ChoreCompletionEntity"
Cohesion: 0.26
Nodes (3): ChoreCompletionDao, Flow, ChoreCompletionEntity

### Community 60 - "LandingViewModel"
Cohesion: 0.18
Nodes (8): Factory, StateFlow, T, ViewModel, ViewModelProvider, LandingUiState, LandingViewModel, ToolCardData

### Community 61 - "RouteHelperEntities.kt"
Cohesion: 0.15
Nodes (5): CachedRoadRouteEntity, PackageWithSequence, RouteType, AMAZON, REGULAR

### Community 62 - "SettingsViewModel"
Cohesion: 0.18
Nodes (11): DebugBridgeUiState, Error, Factory, Idle, StateFlow, T, ViewModel, ViewModelProvider (+3 more)

### Community 63 - "WeekScheduleViewModel"
Cohesion: 0.18
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, WeekScheduleUiState, WeekScheduleViewModel

### Community 65 - "ChoreEntity"
Cohesion: 0.23
Nodes (3): ChoreDao, Flow, ChoreEntity

### Community 67 - "ManageFamilyViewModel"
Cohesion: 0.20
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, ManageFamilyUiState, ManageFamilyViewModel

### Community 69 - "EditSessionViewModel"
Cohesion: 0.20
Nodes (7): EditSessionUiState, EditSessionViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 70 - "NapViewModel"
Cohesion: 0.24
Nodes (4): AndroidViewModel, StateFlow, NapUiState, NapViewModel

### Community 71 - "RoutePlayerScreen.kt"
Cohesion: 0.27
Nodes (10): addArrowMarkers(), calculateBearing(), createArrowIcon(), android, Modifier, MissedPackageAlert(), PlayerMap(), RoutePlayerScreen() (+2 more)

### Community 72 - "VaultHomeViewModel"
Cohesion: 0.21
Nodes (5): AndroidViewModel, StateFlow, Uri, VaultHomeUiState, VaultHomeViewModel

### Community 73 - "api/EssentialsApiClient.kt"
Cohesion: 0.27
Nodes (8): ChoreDto, ChoresResponse, CompletionSyncRequest, CompletionSyncResponse, EssentialsApiClient, Result, PhotoVerificationRequest, PhotoVerificationResponse

### Community 74 - "ChoreDetailViewModel"
Cohesion: 0.21
Nodes (7): ChoreDetailUiState, ChoreDetailViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 75 - "LoginViewModel"
Cohesion: 0.20
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, LoginUiState, LoginViewModel

### Community 76 - "CrashLogger"
Cohesion: 0.18
Nodes (8): CrashLogger, CrashLog, CrashReportingApiClient, CrashLog, Application, CoroutineScope, CrashLogDatabase, DatabaseError

### Community 77 - "SleepSessionEntity"
Cohesion: 0.33
Nodes (3): Flow, SleepSessionDao, SleepSessionEntity

### Community 80 - "PromptStatus"
Cohesion: 0.22
Nodes (7): PromptStatus, COMPLETED, FAILED, IN_PROGRESS, PAUSED, QUEUED, PromptStatusConverter

### Community 81 - "AppDatabase"
Cohesion: 0.07
Nodes (21): AppDatabase, getInstance(), FeatureFunnelDatabase, getInstance(), getInstance(), Context, RoomDatabase, SleepDatabase (+13 more)

### Community 82 - "TigerShapefileParser.kt"
Cohesion: 0.38
Nodes (8): beInt32(), ByteArray, leDouble(), leInt16(), leInt32(), TigerDbfParser, TigerRawRecord, TigerShpParser

### Community 83 - "FeatureFunnelApiClient"
Cohesion: 0.19
Nodes (7): DiscordChannel, Failed, FeatureFunnelApiClient, BaseApiClient, LimitHit, SendPromptResult, Success

### Community 84 - "SleepRepository"
Cohesion: 0.20
Nodes (3): Flow, SleepRepository, toSleepEntry()

### Community 85 - "TimeTrackingDatabase"
Cohesion: 0.27
Nodes (7): getInstance(), insertDefaultDeductions(), Context, RoomDatabase, SupportSQLiteDatabase, migrate(), TimeTrackingDatabase

### Community 86 - "VaultPreferencesRepository"
Cohesion: 0.18
Nodes (5): Keys, Flow, Preferences, VaultPreferencesRepository, VaultConnection

### Community 87 - ".onCreate"
Cohesion: 0.18
Nodes (7): Bundle, ComponentActivity, MainActivity, EssentialsTheme(), Modifier, UpdateUiState, UpdateBanner()

### Community 88 - "Comprehensive App Data Backup System - Implementation Summary"
Cohesion: 0.07
Nodes (28): 1. Dynamic DataStore Discovery (`PreferencesBackup.kt` - NEW), 2. Enhanced Backup Worker (`AppDataBackupWorker.kt` - MODIFIED), 3. Repository Enhancements (MODIFIED), 4. App-Level Integration (`App.kt` - MODIFIED), 5. Restore Capability (`RestoreViewModel.kt` - MODIFIED), 6. Build Success, Architecture Benefits, Comprehensive App Data Backup System - Implementation Summary (+20 more)

### Community 89 - "TimeTrackingHomeViewModel"
Cohesion: 0.24
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, TimeTrackingHomeUiState, TimeTrackingHomeViewModel

### Community 90 - "GeoPoint"
Cohesion: 0.17
Nodes (8): GeoPoint, BuildingFootprintFetcher, firstRingPointOf(), isNumberChar(), ByteArray, quadKeyFor(), RouteDirectionsFetcher, BuildingFootprintFetcherTest

### Community 91 - "ChargingMonitorReceiver"
Cohesion: 0.40
Nodes (4): ChargingMonitorReceiver, BroadcastReceiver, Context, Intent

### Community 92 - "Routes"
Cohesion: 0.20
Nodes (7): ChoreDetail, Home, Login, PhotoCapture, Routes, Settings, Setup

### Community 93 - "DeviceAdminManager"
Cohesion: 0.25
Nodes (3): Activity, DeviceAdminManager, SettingsScreen()

### Community 95 - "HomeControlApiClient"
Cohesion: 0.06
Nodes (30): AccountType, Transaction, PlaidClient, Transaction, TransactionCard(), TransactionListScreen(), HomeControlApiClient, Device (+22 more)

### Community 96 - ".put"
Cohesion: 0.40
Nodes (4): DiscordRichPresenceManager, getInstance(), Context, JSONObject

### Community 97 - "EssentialsDatabase"
Cohesion: 0.25
Nodes (5): EssentialsDatabase, getInstance(), ChoreDao, Context, RoomDatabase

### Community 98 - "EssentialsAdminViewModel"
Cohesion: 0.25
Nodes (6): EssentialsAdminViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 99 - "RouteHelperRouteEntity"
Cohesion: 0.24
Nodes (4): RouteHelperRouteEntity, CreateRouteResult, Failure, Success

### Community 100 - "HomeControlRepository"
Cohesion: 0.10
Nodes (10): HomeControlRepository, Device, DeviceDiscoveryResponse, Result, Room, Routine, RoutineExecutionResponse, StateFlow (+2 more)

### Community 101 - "TigerAddressFetcher"
Cohesion: 0.26
Nodes (6): AddressFetchResult, TigerAddressFetcher, ByteArray, GeoPoint, ZipEntry, ZipFile

### Community 102 - "HistoryViewModel"
Cohesion: 0.25
Nodes (6): Factory, HistoryViewModel, StateFlow, T, ViewModel, ViewModelProvider

### Community 103 - "HomeViewModel"
Cohesion: 0.28
Nodes (7): Factory, HomeUiState, HomeViewModel, StateFlow, T, ViewModel, ViewModelProvider

### Community 104 - "computeWeekSchedule"
Cohesion: 0.39
Nodes (7): computeWeekSchedule(), ScheduledDay, ScheduledRouteStatus, AddScheduleOverrideDialog(), DayCard(), RouteStatusRow(), WeekScheduleScreen()

### Community 105 - "UpdateViewModel"
Cohesion: 0.25
Nodes (6): currentVersionCode(), AndroidViewModel, Context, StateFlow, UpdateUiState, UpdateViewModel

### Community 106 - "UpdateViewModel"
Cohesion: 0.25
Nodes (6): currentVersionCode(), AndroidViewModel, Context, StateFlow, UpdateUiState, UpdateViewModel

### Community 107 - "ActivityMapperHomeScreen"
Cohesion: 0.39
Nodes (7): ActivityMapperHomeScreen(), androidx, Modifier, RichPresenceProfileCard(), RuleCard(), SectionHeader(), VariableCard()

### Community 108 - "DeviceDetailScreen"
Cohesion: 0.24
Nodes (15): androidx, Room, BrightnessControl(), ColorTemperatureControl(), Device, LockControl(), PowerControl(), SensorDisplay() (+7 more)

### Community 109 - "RouteHelperDatabase.kt"
Cohesion: 0.27
Nodes (9): columnExists(), getColumnType(), getInstance(), Context, RoomDatabase, RouteHelperDao, SupportSQLiteDatabase, migrate() (+1 more)

### Community 110 - "SleepSource"
Cohesion: 0.32
Nodes (5): SleepSource, AUTO_DETECTED, MANUAL, NAP, SleepSourceConverter

### Community 111 - "computeDebt"
Cohesion: 0.32
Nodes (4): computeDebt(), NightlyDebt, SleepDebtCalculator, SleepDebtResult

### Community 112 - "NapAlarmScheduler"
Cohesion: 0.46
Nodes (3): Context, NapAlarmScheduler, PendingIntent

### Community 115 - ".fetchLatestRelease"
Cohesion: 0.43
Nodes (5): Failure, ReleaseInfo, Success, UpdateChecker, UpdateCheckResult

### Community 116 - "VaultHomeScreen"
Cohesion: 0.43
Nodes (7): formatLastRun(), isGranted(), android, LoadingButtonContent(), mediaLocationPermission(), mediaReadPermissions(), VaultHomeScreen()

### Community 117 - "EssentialsDeviceAdminReceiver"
Cohesion: 0.39
Nodes (4): DeviceAdminReceiver, EssentialsDeviceAdminReceiver, Context, Intent

### Community 118 - "SetupScreen"
Cohesion: 0.46
Nodes (7): CameraPermissionStep(), DeviceAdminStep(), androidx, OverlayPermissionStep(), PermissionStep(), SetupScreen(), WelcomeStep()

### Community 119 - ".fetchLatestRelease"
Cohesion: 0.43
Nodes (5): Failure, ReleaseInfo, Success, UpdateChecker, UpdateCheckResult

### Community 122 - "TriggerType"
Cohesion: 0.17
Nodes (10): fromString(), LogicalOperator, AND, OR, TriggerType, DEVICE_STATE, MANUAL, SCHEDULE (+2 more)

### Community 123 - "SleepNotifications"
Cohesion: 0.48
Nodes (3): Context, Notification, SleepNotifications

### Community 124 - "NapNotifications"
Cohesion: 0.43
Nodes (3): Context, Notification, NapNotifications

### Community 125 - ".downloadAndInstall"
Cohesion: 0.33
Nodes (4): Context, HttpURLConnection, ReleaseInfo, UpdateInstaller

### Community 126 - "PolylineOffset.kt"
Cohesion: 0.39
Nodes (11): averageBearing(), calculateBearing(), detectTurnaroundIndices(), generateUturnArc(), interpolateBearing(), normalizeBearingDiff(), offsetPoint(), OffsetPolylineResult (+3 more)

### Community 128 - "EssentialsNavGraph"
Cohesion: 0.29
Nodes (4): ChoreDetailScreen(), LoginScreen(), EssentialsNavGraph(), NavHostController

### Community 129 - ".downloadAndInstall"
Cohesion: 0.33
Nodes (4): Context, HttpURLConnection, ReleaseInfo, UpdateInstaller

### Community 132 - "filterAddressGroupsNearBuildings"
Cohesion: 0.28
Nodes (6): InterpolatedAddress, BuildingGrid, cellIndex(), cellKey(), filterAddressGroupsNearBuildings(), BuildingProximityFilterTest

### Community 135 - "LandingPreferencesRepository"
Cohesion: 0.29
Nodes (4): Flow, Preferences, LandingPreferencesRepository, PreferencesKeys

### Community 136 - "AddressMatching.kt"
Cohesion: 0.36
Nodes (10): calculateStreetNameSimilarity(), extractAddressNumber(), extractStreetName(), findBestCandidateMatch(), findBestRoutedStopMatch(), levenshteinDistance(), levenshteinSimilarity(), normalizeAddress() (+2 more)

### Community 137 - "BootCompletedReceiver"
Cohesion: 0.33
Nodes (4): BootCompletedReceiver, BroadcastReceiver, Context, Intent

### Community 138 - "NapAlarmReceiver"
Cohesion: 0.33
Nodes (4): BroadcastReceiver, Context, Intent, NapAlarmReceiver

### Community 140 - "Exception"
Cohesion: 0.19
Nodes (9): ActivityMapperApiClient, JSONObject, Result, AutomationRule, DiscordRichPresenceProfile, VariableValue, EditRichPresenceProfileScreen(), T (+1 more)

### Community 142 - "RouteEditViewModel"
Cohesion: 0.20
Nodes (4): AndroidViewModel, StateFlow, RouteEditUiState, RouteEditViewModel

### Community 144 - "ConditionOperator"
Cohesion: 0.25
Nodes (8): ConditionOperator, EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, NOT_EQUALS, fromSymbol()

### Community 147 - "PackageCleanupWorker"
Cohesion: 0.40
Nodes (3): CoroutineWorker, Result, PackageCleanupWorker

### Community 149 - ".getAccounts"
Cohesion: 0.36
Nodes (4): BankingApiClient, Result, PlaidAccessToken, PlaidLinkToken

### Community 151 - "ChoreCard"
Cohesion: 0.67
Nodes (3): ChoreCard(), EssentialsAdminHome(), com

### Community 152 - "EditSessionScreen"
Cohesion: 0.83
Nodes (3): EditSessionScreen(), formatInstant(), InstantPickerDialog()

### Community 153 - "NapScreen"
Cohesion: 0.83
Nodes (3): formatDuration(), formatEpochMillis(), NapScreen()

### Community 154 - "sleep/ui/settings/SettingsScreen.kt"
Cohesion: 0.83
Nodes (3): formatMinutesOfDay(), SettingsScreen(), WakeTimePickerDialog()

### Community 155 - "UpdateBanner"
Cohesion: 0.50
Nodes (3): Modifier, UpdateUiState, UpdateBanner()

### Community 156 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 157 - "HomeControlViewModel"
Cohesion: 0.08
Nodes (25): Factory, Flow, TransactionListViewModel, DevicesTab(), HomeControlHomeScreen(), Room, Routine, OverviewTab() (+17 more)

### Community 172 - "App"
Cohesion: 0.39
Nodes (7): App, EssentialsRepository, FeatureFunnelRepository, RouteHelperRepository, SleepRepository, TimeTrackingRepository, UserPreferencesRepository

### Community 174 - "stickyNearestSlots"
Cohesion: 0.35
Nodes (4): T, nextOrNull(), stickyNearestSlots(), StickyNearestSlotsTest

### Community 177 - "BankingDatabase"
Cohesion: 0.33
Nodes (5): BankingDatabase, getInstance(), Context, RoomDatabase, BudgetDao

### Community 179 - ".findOptimalInsertionPoint"
Cohesion: 0.62
Nodes (3): GeoPoint, NonRoutableOptimizer, toGeoPoint()

### Community 181 - "HomeControlModels.kt"
Cohesion: 0.12
Nodes (4): ColorValue, DeviceCommandRequest, DeviceDiscoveryResponse, RoutineExecutionResponse

### Community 189 - "Capability"
Cohesion: 0.14
Nodes (13): Capability, BATTERY, BRIGHTNESS, COLOR, COLOR_TEMPERATURE, CONTACT, HUMIDITY, LOCK (+5 more)

### Community 191 - "RoutineBuilderScreen"
Cohesion: 0.30
Nodes (13): Device, RoutineAction, RoutineCondition, RoutineTrigger, ActionCard(), AddActionDialog(), AddConditionDialog(), AddTriggerDialog() (+5 more)

### Community 198 - "DeviceType"
Cohesion: 0.15
Nodes (13): DeviceType, LIGHT, LOCK, PLUG, SCENE, SENSOR, SWITCH, THERMOSTAT (+5 more)

### Community 199 - "IsaacsHubScaffold"
Cohesion: 0.06
Nodes (39): android, AccountCard(), AccountsList(), BankingHomeScreen(), EmptyState(), BankAccount, DeviceListScreen(), CameraPreview() (+31 more)

### Community 202 - "PayType"
Cohesion: 0.32
Nodes (4): PayType, EVALUATION, HOURLY, PayTypeConverter

### Community 203 - ".checkForCompletion"
Cohesion: 0.29
Nodes (5): FeatureFunnelWorker, Result, CoroutineWorker, FeatureFunnelPreferences, FeatureFunnelPreferencesRepository

### Community 205 - "RoutineCard"
Cohesion: 0.43
Nodes (6): Routine, getTriggerDescription(), getTriggerIcon(), androidx, RoutineCard(), RoutineListScreen()

### Community 206 - "parseScannedAddresses"
Cohesion: 0.27
Nodes (3): parseScannedAddresses(), ScannedAddress, MailScanParserTest

### Community 216 - "LandingScreen.kt"
Cohesion: 0.60
Nodes (4): LandingCard, LandingScreen(), ToolCard(), ImageVector

### Community 221 - "MailScanScreen"
Cohesion: 0.47
Nodes (5): ImageProxy, TextRecognizer, MailCameraPreview(), MailScanScreen(), processImageProxy()

### Community 226 - "PhotoCaptureScreen"
Cohesion: 0.53
Nodes (5): CameraPreviewScreen(), Modifier, Uri, PhotoCaptureScreen(), PhotoPreviewScreen()

## Knowledge Gaps
- **189 isolated node(s):** `Overview`, `Problem Solved`, `1. Dynamic DataStore Discovery (`PreferencesBackup.kt` - NEW)`, `2. Enhanced Backup Worker (`AppDataBackupWorker.kt` - MODIFIED)`, `3. Repository Enhancements (MODIFIED)` (+184 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **64 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `GeoPoint` connect `GeoPoint` to `AddressInterpolationTest`, `TigerShapefileParserTest`, `filterAddressGroupsNearBuildings`, `.httpRequest`, `CandidateAddress`, `RoutePlayerScreen.kt`, `MailScanViewModel`, `LocationSample`, `TigerShapefileParser.kt`, `IndianaAddressPointFetcher`, `MailScanScreen`, `PolylineOffset.kt`?**
  _High betweenness centrality (0.237) - this node is a cross-community bridge._
- **Why does `RoutePlayerScreen()` connect `RoutePlayerScreen.kt` to `MailScanScreen`, `PackageScanScreen.kt`, `RoutedStopEntity`?**
  _High betweenness centrality (0.198) - this node is a cross-community bridge._
- **Why does `processPackageImage()` connect `PackageScanScreen.kt` to `IsaacsHubScaffold`?**
  _High betweenness centrality (0.172) - this node is a cross-community bridge._
- **Are the 16 inferred relationships involving `GeoPoint` (e.g. with `toSample()` and `.queryPage()`) actually correct?**
  _`GeoPoint` has 16 INFERRED edges - model-reasoned connections that need verification._
- **Are the 6 inferred relationships involving `HomeControlRepository` (e.g. with `DeviceDetailScreen()` and `DeviceListScreen()`) actually correct?**
  _`HomeControlRepository` has 6 INFERRED edges - model-reasoned connections that need verification._
- **Are the 6 inferred relationships involving `HomeControlApiClient` (e.g. with `DeviceDetailScreen()` and `DeviceListScreen()`) actually correct?**
  _`HomeControlApiClient` has 6 INFERRED edges - model-reasoned connections that need verification._
- **What connects `Overview`, `Problem Solved`, `1. Dynamic DataStore Discovery (`PreferencesBackup.kt` - NEW)` to the rest of the system?**
  _189 weakly-connected nodes found - possible documentation gaps or missing edges._