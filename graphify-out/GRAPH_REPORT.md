# Graph Report - isaacs-hub  (2026-07-29)

## Corpus Check
- 316 files · ~121,264 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 2382 nodes · 3740 edges · 189 communities (154 shown, 35 thin omitted)
- Extraction: 91% EXTRACTED · 9% INFERRED · 0% AMBIGUOUS · INFERRED: 320 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8a2b6c74`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- AddressInterpolationTest
- EssentialsApi
- MediaItem
- EssentialsRepository
- SleepDetectionEngine
- RoutePlayerViewModel
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
- BudgetDao
- RouteScheduleOverrideEntity
- TimeEntryEntity
- HomeViewModel
- .put
- RoutedStopEntity
- AppBlockingService
- Exception
- .prefs
- .getAccounts
- RouteEntity
- ActivityMapperModels.kt
- ActivityMapperRepository
- PlaidConnectionSetupViewModel
- IndianaAddressPointFetcher
- TimeTrackerSettingsViewModel
- ChildAccountEntity
- FeaturePromptEntity
- NapAlarmService
- EditTimeEntryViewModel
- Flow
- UserPreferencesRepository
- FeatureFunnelHomeViewModel
- Routes
- computeWeeklySummary
- formatNumber
- PhotoCaptureViewModel
- CompletionStatus
- EssentialsRepository
- FeatureFunnelPreferencesRepository
- RouteHelperRouteEntity
- RestoreViewModel
- AuthRepository
- ActivityMapperViewModel
- TransactionDao
- BudgetCategory
- BudgetConfigViewModel
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
- SettingsViewModel
- VaultHomeViewModel
- api/EssentialsApiClient.kt
- ChoreDetailViewModel
- LoginViewModel
- VariableType
- calculateBudgetState
- BankAccount
- CLAUDE.md
- PromptStatus
- AppDatabase
- TigerShapefileParser.kt
- filterAddressGroupsNearBuildings
- SleepSessionEntity
- TimeTrackingDatabase
- parsePairingPayload
- .onCreate
- WorkDatabase
- TimeTrackingHomeViewModel
- VaultPreferencesRepository
- ChargingMonitorReceiver
- Routes
- DeviceAdminManager
- firstRingPointOf
- .fetchAddressesForZip
- DiscordRichPresenceManager
- EssentialsDatabase
- EssentialsAdminViewModel
- .createAmazonRoute
- DebugTriggerResult
- SleepSessionDao
- HistoryViewModel
- HomeViewModel
- computeWeekSchedule
- UpdateViewModel
- UpdateViewModel
- ActivityMapperHomeScreen
- GeoPoint
- RouteHelperDatabase
- SleepSource
- computeDebt
- NapAlarmScheduler
- PolylineOffset.kt
- RouteSectionEntity
- .fetchLatestRelease
- VaultHomeScreen
- EssentialsDeviceAdminReceiver
- SetupScreen
- .fetchLatestRelease
- ChoreEntity.kt
- FeatureFunnelScheduler
- PlaidClient
- SleepNotifications
- NapNotifications
- .downloadAndInstall
- AppDataBackupScheduler
- PhotoBackupScheduler
- EssentialsNavGraph
- .downloadAndInstall
- EnergyForecastCalculatorTest
- WindDownCalculatorTest
- stickyNearestSlots
- DatabaseMigrationHelper
- FeatureFunnelDatabase
- LandingPreferencesRepository
- SleepDatabase
- BootCompletedReceiver
- NapAlarmReceiver
- processImageProxy
- PairingViewModel
- StopSideTest
- RouteEditViewModel
- DebugLogger
- LandingScreen
- CountyFipsLookup
- PackageCleanupScheduler
- PackageCleanupWorker
- SleepDetectionController
- VaultApiClient
- WindDownCalculator
- ChoreCard
- EditSessionScreen
- NapScreen
- sleep/ui/settings/SettingsScreen.kt
- UpdateBanner
- gradlew
- FeatureFunnelHomeScreen
- HistoryScreen
- planPendingUploads
- PlaidConfig.kt
- deploy-apk.sh
- TransactionListViewModel
- DeductionType
- preferencesToJson
- .doWork
- .doWork
- BankingDatabase
- Result
- ManualUploadWorker
- Transaction
- JSONObject
- com
- NumberFormat
- Context
- RoomDatabase
- Modifier
- NavHostController

## God Nodes (most connected - your core abstractions)
1. `GeoPoint` - 74 edges
2. `RouteHelperRepository` - 44 edges
3. `RouteHelperDao` - 41 edges
4. `RoutePlayerViewModel` - 29 edges
5. `RoutedStopEntity` - 27 edges
6. `BankingRepository` - 24 edges
7. `ActivityMapperApiClient` - 24 edges
8. `FeaturePromptEntity` - 23 edges
9. `RouteEntity` - 22 edges
10. `ActivityMapperRepository` - 21 edges

## Surprising Connections (you probably didn't know these)
- `toSleepEntry()` --references--> `SleepEntry`  [EXTRACTED]
  app/src/main/java/com/isaacshub/app/sleep/data/SleepRepository.kt → sleepcore/src/main/kotlin/com/isaacshub/sleep/core/SleepDebt.kt
- `TransactionListScreen()` --calls--> `PlaidClient`  [INFERRED]
  app/src/main/java/com/isaacshub/app/banking/ui/transactions/TransactionListScreen.kt → app/src/main/java/com/isaacshub/app/banking/data/PlaidClient.kt
- `BankingHomeScreen()` --calls--> `BankingRepository`  [INFERRED]
  app/src/main/java/com/isaacshub/app/banking/ui/home/BankingHomeScreen.kt → app/src/main/java/com/isaacshub/app/banking/data/BankingRepository.kt
- `PlaidConnectionSetupScreen()` --calls--> `BankingRepository`  [INFERRED]
  app/src/main/java/com/isaacshub/app/banking/ui/setup/PlaidConnectionSetupScreen.kt → app/src/main/java/com/isaacshub/app/banking/data/BankingRepository.kt
- `TransactionListScreen()` --calls--> `BankingRepository`  [INFERRED]
  app/src/main/java/com/isaacshub/app/banking/ui/transactions/TransactionListScreen.kt → app/src/main/java/com/isaacshub/app/banking/data/BankingRepository.kt

## Import Cycles
- None detected.

## Communities (189 total, 35 thin omitted)

### Community 0 - "AddressInterpolationTest"
Cohesion: 0.22
Nodes (9): HouseNumberRange, interpolateAddresses(), interpolateAddressGroups(), pointAtDistance(), pointsAlongLine(), sampleHouseNumbers(), sideAddresses(), TigerAddressFeature (+1 more)

### Community 1 - "EssentialsApi"
Cohesion: 0.05
Nodes (31): EssentialsApi, EssentialsEndpointPaths, Result, Chore, CreateChoreRequest, UpdateChoreRequest, AdminOverrideRequest, ChoreCompletion (+23 more)

### Community 2 - "MediaItem"
Cohesion: 0.24
Nodes (7): MediaItem, MediaType, IMAGE, VIDEO, remoteFolderFor(), Uri, MediaStoreScanner

### Community 3 - "EssentialsRepository"
Cohesion: 0.05
Nodes (22): ChoreDao, Flow, CompletionDao, Flow, DayOfWeekListConverter, LocalChoreEntity, CompletionStatus, COMPLETED (+14 more)

### Community 4 - "SleepDetectionEngine"
Cohesion: 0.08
Nodes (23): IBinder, Intent, Service, SharedPreferences, SleepDetectionService, SensorManager, DetectionConfig, DetectionEvent (+15 more)

### Community 5 - "RoutePlayerViewModel"
Cohesion: 0.07
Nodes (19): ApiException, ChildAccountDto, ChoreDto, EssentialsApiClient, JSONObject, Result, T, NominatimGeocoder (+11 more)

### Community 6 - "PackageScanScreen.kt"
Cohesion: 0.07
Nodes (44): Modifier, LiveMap(), RouteBuilderScreen(), AddressActionCard(), SideButton(), Context, newOsmMapView(), ImageProxy (+36 more)

### Community 7 - "CandidateAddress"
Cohesion: 0.14
Nodes (9): distanceMeters(), CandidateAddress, nearestAddresses(), AndroidViewModel, StateFlow, RouteHelperTestUiState, RouteHelperTestViewModel, TestStop (+1 more)

### Community 8 - "AmazonRouteScannerViewModel"
Cohesion: 0.10
Nodes (24): extractAddress(), ExtractedAddress, extractFromDirectionSheet(), isValidStreetAddress(), AddressCameraScanner(), AddressListItem(), AmazonRouteScannerScreen(), com (+16 more)

### Community 9 - "EditRouteViewModel"
Cohesion: 0.08
Nodes (18): ScheduleType, BIWEEKLY, DAILY_MON_FRI, DAILY_SAT_FRI, NONE, WEEKLY, ScheduleTypeConverter, AnchorDatePickerDialog() (+10 more)

### Community 10 - "MailScanViewModel"
Cohesion: 0.11
Nodes (9): parseScannedAddresses(), ScannedAddress, AndroidViewModel, ScannedAddress, StateFlow, MailScanUiState, MailScanViewModel, ResolvedMailStop (+1 more)

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

### Community 18 - "BudgetDao"
Cohesion: 0.17
Nodes (5): BudgetAccountSelectionEntity, BudgetCategoryEntity, BudgetDao, Flow, toEntity()

### Community 19 - "RouteScheduleOverrideEntity"
Cohesion: 0.11
Nodes (10): Flow, RouteScheduleOverrideDao, RouteScheduleOverrideEntity, currentPayPeriodRange(), ClosedRange, computePayPeriodSummary(), PayPeriodSummary, ClosedRange (+2 more)

### Community 20 - "TimeEntryEntity"
Cohesion: 0.13
Nodes (5): Flow, TimeEntryDao, TimeEntryEntity, Flow, TimeTrackingRepository

### Community 21 - "HomeViewModel"
Cohesion: 0.12
Nodes (13): ChoreCard(), CompletionProgressCard(), HomeScreen(), Modifier, ChoreUiState, Factory, HomeUiState, HomeViewModel (+5 more)

### Community 22 - ".put"
Cohesion: 0.19
Nodes (9): BaseApiClient, HttpURLConnection, JSONObject, MultiUrlApiClient, Failed, FeatureFunnelApiClient, LimitHit, SendPromptResult (+1 more)

### Community 23 - "RoutedStopEntity"
Cohesion: 0.15
Nodes (7): RoutedStopEntity, AddSectionDialog(), RouteEditScreen(), SectionRow(), StopRow(), AmazonLoadTruckScreen(), LoadTruckStopItem()

### Community 24 - "AppBlockingService"
Cohesion: 0.16
Nodes (10): AppBlockingService, Context, IBinder, Intent, Notification, Service, View, WindowManager (+2 more)

### Community 25 - "Exception"
Cohesion: 0.20
Nodes (8): ActivityMapperApiClient, JSONObject, Result, DiscordRichPresenceProfile, VariableValue, EditRichPresenceProfileScreen(), T, Exception

### Community 26 - ".prefs"
Cohesion: 0.16
Nodes (10): Context, NapAlarmController, Context, SharedPreferences, NapPhase, IDLE, NAPPING, RINGING (+2 more)

### Community 27 - ".getAccounts"
Cohesion: 0.36
Nodes (4): BankingApiClient, Result, PlaidAccessToken, PlaidLinkToken

### Community 28 - "RouteEntity"
Cohesion: 0.13
Nodes (9): Flow, RouteDao, RouteEntity, Factory, StateFlow, T, ViewModel, ViewModelProvider (+1 more)

### Community 29 - "ActivityMapperModels.kt"
Cohesion: 0.12
Nodes (20): Action, ActionType, SET_DISCORD_RICH_PRESENCE, ActivityMapperData, ComparisonOperator, EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL (+12 more)

### Community 30 - "ActivityMapperRepository"
Cohesion: 0.27
Nodes (4): AutomationRule, ActivityMapperRepository, Result, StateFlow

### Community 31 - "PlaidConnectionSetupViewModel"
Cohesion: 0.13
Nodes (13): CreatingLinkToken, Error, ExchangingToken, Factory, Idle, StateFlow, T, ViewModel (+5 more)

### Community 32 - "IndianaAddressPointFetcher"
Cohesion: 0.13
Nodes (8): AddressFetchResult, Failure, FetchedAddress, Success, IndianaAddressPointFetcher, JSONObject, RouteHelperAddressFetcher, IndianaAddressPointFetcherTest

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
Cohesion: 0.10
Nodes (11): PayType, EVALUATION, HOURLY, PayTypeConverter, EditTimeEntryUiState, EditTimeEntryViewModel, Factory, StateFlow (+3 more)

### Community 38 - "Flow"
Cohesion: 0.27
Nodes (4): BankAccount, Flow, BudgetCategory, BudgetState

### Community 39 - "UserPreferencesRepository"
Cohesion: 0.14
Nodes (5): Keys, Flow, Preferences, UserPreferences, UserPreferencesRepository

### Community 40 - "FeatureFunnelHomeViewModel"
Cohesion: 0.14
Nodes (7): Factory, FeatureFunnelHomeViewModel, FeatureFunnelUiState, StateFlow, T, ViewModel, ViewModelProvider

### Community 42 - "computeWeeklySummary"
Cohesion: 0.28
Nodes (8): computeCarryoverHours(), computeWeeklySummary(), currentWeekRange(), ClosedRange, localDate(), weekStartFor(), hourlyEntry(), WeeklySummaryTest

### Community 43 - "formatNumber"
Cohesion: 0.11
Nodes (17): WeeklySummary, Modifier, PayPeriodSummaryCard(), TimeEntryRow(), Modifier, WeeklyHoursBar(), EditTimeEntryScreen(), EntryDatePickerDialog() (+9 more)

### Community 44 - "PhotoCaptureViewModel"
Cohesion: 0.11
Nodes (14): CameraPreviewScreen(), Modifier, Uri, PhotoCaptureScreen(), PhotoPreviewScreen(), Factory, Context, StateFlow (+6 more)

### Community 45 - "CompletionStatus"
Cohesion: 0.15
Nodes (10): CompletionStatus, COMPLETED, FAILED, IN_PROGRESS, NOT_STARTED, PENDING_VERIFICATION, REJECTED, VERIFIED (+2 more)

### Community 47 - "FeatureFunnelPreferencesRepository"
Cohesion: 0.16
Nodes (7): FeatureFunnelPreferences, FeatureFunnelPreferencesRepository, Keys, Flow, FeatureFunnelWorker, CoroutineWorker, Result

### Community 48 - "RouteHelperRouteEntity"
Cohesion: 0.18
Nodes (5): RouteHelperRouteEntity, AndroidViewModel, StateFlow, NewRouteUiState, RouteHelperHomeViewModel

### Community 49 - "RestoreViewModel"
Cohesion: 0.16
Nodes (10): RestoreScreen(), BackupDatabase, Error, Idle, StateFlow, ViewModel, Loading, RestoreState (+2 more)

### Community 50 - "AuthRepository"
Cohesion: 0.20
Nodes (6): AuthRepository, AuthToken, Result, EssentialsApp, Application, EssentialsRepository

### Community 51 - "ActivityMapperViewModel"
Cohesion: 0.15
Nodes (7): ActivityMapperUiState, ActivityMapperViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 52 - "TransactionDao"
Cohesion: 0.22
Nodes (4): Flow, TransactionDao, fromDomain(), TransactionEntity

### Community 53 - "BudgetCategory"
Cohesion: 0.21
Nodes (7): BudgetCategory, toDomain(), AccountSelectionDialog(), CategoryEditDialog(), BudgetCategoryConfigCard(), BudgetConfigScreen(), NumberFormat

### Community 54 - "BudgetConfigViewModel"
Cohesion: 0.18
Nodes (7): BudgetConfigUiState, BudgetConfigViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 55 - "BankingHomeViewModel"
Cohesion: 0.16
Nodes (10): BankingHomeViewModel, BankingUiState, Error, Factory, StateFlow, T, ViewModel, ViewModelProvider (+2 more)

### Community 57 - "EditPromptViewModel"
Cohesion: 0.18
Nodes (7): EditPromptUiState, EditPromptViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 58 - "BankingRepository"
Cohesion: 0.19
Nodes (4): BankingRepository, Result, BankConnection, BankConnectionEntity

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

### Community 68 - "SleepEntry"
Cohesion: 0.24
Nodes (3): toSleepEntry(), SleepEntry, SleepDebtCalculatorTest

### Community 69 - "EditSessionViewModel"
Cohesion: 0.20
Nodes (7): EditSessionUiState, EditSessionViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 70 - "NapViewModel"
Cohesion: 0.24
Nodes (4): AndroidViewModel, StateFlow, NapUiState, NapViewModel

### Community 71 - "SettingsViewModel"
Cohesion: 0.18
Nodes (6): Factory, StateFlow, T, ViewModel, ViewModelProvider, SettingsViewModel

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

### Community 76 - "VariableType"
Cohesion: 0.29
Nodes (7): VariableType, CURRENT_STOP_NUMBER, ROUTE_COMPLETION_PERCENT, ROUTE_PLAY_MODE_ACTIVE, SLEEP_TIME_REMAINING, SLEEPING, TOTAL_STOPS

### Community 77 - "calculateBudgetState"
Cohesion: 0.27
Nodes (7): BudgetState, calculateBudgetState(), CategoryFillState, BudgetCategoryBar(), Modifier, BudgetTowerCard(), Modifier

### Community 80 - "PromptStatus"
Cohesion: 0.22
Nodes (7): PromptStatus, COMPLETED, FAILED, IN_PROGRESS, PAUSED, QUEUED, PromptStatusConverter

### Community 81 - "AppDatabase"
Cohesion: 0.15
Nodes (8): AppDatabase, getInstance(), BankAccountDao, BankConnectionDao, Context, FeaturePromptDao, RoomDatabase, SleepSessionDao

### Community 82 - "TigerShapefileParser.kt"
Cohesion: 0.38
Nodes (8): beInt32(), ByteArray, leDouble(), leInt16(), leInt32(), TigerDbfParser, TigerRawRecord, TigerShpParser

### Community 83 - "filterAddressGroupsNearBuildings"
Cohesion: 0.28
Nodes (6): InterpolatedAddress, BuildingGrid, cellIndex(), cellKey(), filterAddressGroupsNearBuildings(), BuildingProximityFilterTest

### Community 84 - "SleepSessionEntity"
Cohesion: 0.33
Nodes (3): Flow, SleepRepository, SleepSessionEntity

### Community 85 - "TimeTrackingDatabase"
Cohesion: 0.24
Nodes (7): getInstance(), insertDefaultDeductions(), Context, RoomDatabase, SupportSQLiteDatabase, migrate(), TimeTrackingDatabase

### Community 86 - "parsePairingPayload"
Cohesion: 0.30
Nodes (3): parsePairingPayload(), VaultConnection, VaultConnectionTest

### Community 87 - ".onCreate"
Cohesion: 0.18
Nodes (7): Bundle, ComponentActivity, MainActivity, EssentialsTheme(), Modifier, UpdateUiState, UpdateBanner()

### Community 88 - "WorkDatabase"
Cohesion: 0.22
Nodes (6): getInstance(), insertDefaultDeductions(), Context, RoomDatabase, SupportSQLiteDatabase, WorkDatabase

### Community 89 - "TimeTrackingHomeViewModel"
Cohesion: 0.24
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, TimeTrackingHomeUiState, TimeTrackingHomeViewModel

### Community 90 - "VaultPreferencesRepository"
Cohesion: 0.16
Nodes (6): App, Application, EssentialsRepository, Keys, Flow, VaultPreferencesRepository

### Community 91 - "ChargingMonitorReceiver"
Cohesion: 0.40
Nodes (4): ChargingMonitorReceiver, BroadcastReceiver, Context, Intent

### Community 92 - "Routes"
Cohesion: 0.20
Nodes (7): ChoreDetail, Home, Login, PhotoCapture, Routes, Settings, Setup

### Community 93 - "DeviceAdminManager"
Cohesion: 0.25
Nodes (3): Activity, DeviceAdminManager, SettingsScreen()

### Community 94 - "firstRingPointOf"
Cohesion: 0.21
Nodes (6): BuildingFootprintFetcher, firstRingPointOf(), isNumberChar(), ByteArray, quadKeyFor(), BuildingFootprintFetcherTest

### Community 95 - ".fetchAddressesForZip"
Cohesion: 0.18
Nodes (8): AddressParity, BOTH, EVEN, ODD, UNKNOWN, parseParity(), ByteArray, TigerAddressFetcher

### Community 96 - "DiscordRichPresenceManager"
Cohesion: 0.42
Nodes (4): DiscordRichPresenceManager, getInstance(), Context, JSONObject

### Community 97 - "EssentialsDatabase"
Cohesion: 0.25
Nodes (5): EssentialsDatabase, getInstance(), ChoreDao, Context, RoomDatabase

### Community 98 - "EssentialsAdminViewModel"
Cohesion: 0.25
Nodes (6): EssentialsAdminViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 99 - ".createAmazonRoute"
Cohesion: 0.33
Nodes (4): CreateRouteResult, Failure, ScannedAddressData, Success

### Community 100 - "DebugTriggerResult"
Cohesion: 0.36
Nodes (5): AlreadyRunning, DebugTriggerResult, Failed, SettingsApiClient, Started

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

### Community 109 - "RouteHelperDatabase"
Cohesion: 0.29
Nodes (6): getInstance(), Context, RoomDatabase, SupportSQLiteDatabase, migrate(), RouteHelperDatabase

### Community 110 - "SleepSource"
Cohesion: 0.32
Nodes (5): SleepSource, AUTO_DETECTED, MANUAL, NAP, SleepSourceConverter

### Community 111 - "computeDebt"
Cohesion: 0.32
Nodes (4): computeDebt(), NightlyDebt, SleepDebtCalculator, SleepDebtResult

### Community 112 - "NapAlarmScheduler"
Cohesion: 0.46
Nodes (3): Context, NapAlarmScheduler, PendingIntent

### Community 113 - "PolylineOffset.kt"
Cohesion: 0.39
Nodes (11): averageBearing(), calculateBearing(), detectTurnaroundIndices(), generateUturnArc(), interpolateBearing(), normalizeBearingDiff(), offsetPoint(), OffsetPolylineResult (+3 more)

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

### Community 122 - "PlaidClient"
Cohesion: 0.09
Nodes (25): AccountType, PlaidClient, AccountCard(), AccountsList(), BankingHomeScreen(), EmptyState(), BankAccount, PlaidConnectionSetupScreen() (+17 more)

### Community 123 - "SleepNotifications"
Cohesion: 0.48
Nodes (3): Context, Notification, SleepNotifications

### Community 124 - "NapNotifications"
Cohesion: 0.43
Nodes (3): Context, Notification, NapNotifications

### Community 125 - ".downloadAndInstall"
Cohesion: 0.33
Nodes (4): Context, HttpURLConnection, ReleaseInfo, UpdateInstaller

### Community 128 - "EssentialsNavGraph"
Cohesion: 0.29
Nodes (4): ChoreDetailScreen(), LoginScreen(), EssentialsNavGraph(), NavHostController

### Community 129 - ".downloadAndInstall"
Cohesion: 0.33
Nodes (4): Context, HttpURLConnection, ReleaseInfo, UpdateInstaller

### Community 132 - "stickyNearestSlots"
Cohesion: 0.35
Nodes (4): T, nextOrNull(), stickyNearestSlots(), StickyNearestSlotsTest

### Community 134 - "FeatureFunnelDatabase"
Cohesion: 0.40
Nodes (4): FeatureFunnelDatabase, getInstance(), Context, RoomDatabase

### Community 135 - "LandingPreferencesRepository"
Cohesion: 0.33
Nodes (3): Flow, LandingPreferencesRepository, PreferencesKeys

### Community 136 - "SleepDatabase"
Cohesion: 0.40
Nodes (4): getInstance(), Context, RoomDatabase, SleepDatabase

### Community 137 - "BootCompletedReceiver"
Cohesion: 0.33
Nodes (4): BootCompletedReceiver, BroadcastReceiver, Context, Intent

### Community 138 - "NapAlarmReceiver"
Cohesion: 0.33
Nodes (4): BroadcastReceiver, Context, Intent, NapAlarmReceiver

### Community 139 - "processImageProxy"
Cohesion: 0.47
Nodes (5): CameraPreview(), ImageProxy, PairingScreen(), processImageProxy(), BarcodeScanner

### Community 140 - "PairingViewModel"
Cohesion: 0.40
Nodes (4): AndroidViewModel, StateFlow, PairingUiState, PairingViewModel

### Community 142 - "RouteEditViewModel"
Cohesion: 0.20
Nodes (4): AndroidViewModel, StateFlow, RouteEditUiState, RouteEditViewModel

### Community 144 - "LandingScreen"
Cohesion: 0.60
Nodes (4): LandingCard, LandingScreen(), ToolCard(), ImageVector

### Community 147 - "PackageCleanupWorker"
Cohesion: 0.40
Nodes (3): CoroutineWorker, Result, PackageCleanupWorker

### Community 149 - "VaultApiClient"
Cohesion: 0.31
Nodes (3): VaultApiClient, RandomAccessFile, URL

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

### Community 172 - "TransactionListViewModel"
Cohesion: 0.29
Nodes (6): Factory, Flow, TransactionListViewModel, T, ViewModel, ViewModelProvider

### Community 173 - "DeductionType"
Cohesion: 0.32
Nodes (4): DeductionType, FLAT, PERCENT, DeductionTypeConverter

### Community 174 - "preferencesToJson"
Cohesion: 0.32
Nodes (3): Preferences, preferencesToJson(), PreferencesJsonTest

### Community 175 - ".doWork"
Cohesion: 0.33
Nodes (4): AppDataBackupWorker, CoroutineWorker, Result, RoomDatabase

### Community 176 - ".doWork"
Cohesion: 0.29
Nodes (4): PendingUpload, CoroutineWorker, Result, PhotoBackupWorker

### Community 177 - "BankingDatabase"
Cohesion: 0.40
Nodes (4): BankingDatabase, getInstance(), Context, RoomDatabase

### Community 179 - "ManualUploadWorker"
Cohesion: 0.40
Nodes (3): CoroutineWorker, Result, ManualUploadWorker

### Community 180 - "Transaction"
Cohesion: 0.33
Nodes (3): Transaction, TransactionCard(), TransactionListScreen()

## Knowledge Gaps
- **126 isolated node(s):** `Persistent Memory`, `Context Navigation`, `ROUTE_PLAY_MODE_ACTIVE`, `ROUTE_COMPLETION_PERCENT`, `SLEEP_TIME_REMAINING` (+121 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **35 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `GeoPoint` connect `GeoPoint` to `AddressInterpolationTest`, `IndianaAddressPointFetcher`, `TigerShapefileParserTest`, `RoutePlayerViewModel`, `PackageScanScreen.kt`, `CandidateAddress`, `MailScanViewModel`, `LocationSample`, `PolylineOffset.kt`, `TigerShapefileParser.kt`, `filterAddressGroupsNearBuildings`, `RoutedStopEntity`, `RouteHelperEntities.kt`, `firstRingPointOf`, `.fetchAddressesForZip`?**
  _High betweenness centrality (0.188) - this node is a cross-community bridge._
- **Why does `TimeTrackingRepository` connect `TimeEntryEntity` to `TimeTrackerSettingsViewModel`, `VaultPreferencesRepository`, `RouteScheduleOverrideEntity`, `RouteEntity`?**
  _High betweenness centrality (0.127) - this node is a cross-community bridge._
- **Why does `RouteHelperRepository` connect `RouteHelperRepository` to `.createAmazonRoute`, `RouteHelperDao`, `RouteHelperRouteEntity`, `RouteSectionEntity`, `RoutedStopEntity`, `VaultPreferencesRepository`, `RouteHelperEntities.kt`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **Are the 16 inferred relationships involving `GeoPoint` (e.g. with `toSample()` and `.queryPage()`) actually correct?**
  _`GeoPoint` has 16 INFERRED edges - model-reasoned connections that need verification._
- **What connects `Persistent Memory`, `Context Navigation`, `ROUTE_PLAY_MODE_ACTIVE` to the rest of the system?**
  _126 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `EssentialsApi` be split into smaller, more focused modules?**
  _Cohesion score 0.05129561078794289 - nodes in this community are weakly interconnected._
- **Should `EssentialsRepository` be split into smaller, more focused modules?**
  _Cohesion score 0.05027322404371585 - nodes in this community are weakly interconnected._