# Graph Report - .  (2026-07-28)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 2323 nodes · 3716 edges · 172 communities (148 shown, 24 thin omitted)
- Extraction: 90% EXTRACTED · 10% INFERRED · 0% AMBIGUOUS · INFERRED: 355 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `21e66450`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Community 0
- Community 1
- Community 2
- Community 3
- Community 4
- Community 5
- Community 6
- Community 7
- Community 8
- Community 9
- Community 10
- Community 11
- Community 12
- Community 13
- Community 14
- Community 15
- Community 16
- Community 17
- Community 18
- Community 19
- Community 20
- Community 21
- Community 22
- Community 23
- Community 24
- Community 25
- Community 26
- Community 27
- Community 28
- Community 29
- Community 30
- Community 31
- Community 32
- Community 33
- Community 34
- Community 35
- Community 36
- Community 37
- Community 38
- Community 39
- Community 40
- Community 41
- Community 42
- Community 43
- Community 44
- Community 45
- Community 46
- Community 47
- Community 48
- Community 49
- Community 50
- Community 51
- Community 52
- Community 53
- Community 54
- Community 55
- Community 56
- Community 57
- Community 58
- Community 59
- Community 60
- Community 61
- Community 62
- Community 63
- Community 64
- Community 65
- Community 66
- Community 67
- Community 68
- Community 69
- Community 70
- Community 71
- Community 72
- Community 73
- Community 74
- Community 75
- Community 76
- Community 77
- Community 78
- Community 79
- Community 80
- Community 81
- Community 82
- Community 83
- Community 84
- Community 85
- Community 86
- Community 87
- Community 88
- Community 89
- Community 90
- Community 91
- Community 92
- Community 93
- Community 94
- Community 95
- Community 96
- Community 97
- Community 98
- Community 99
- Community 100
- Community 101
- Community 102
- Community 103
- Community 104
- Community 105
- Community 106
- Community 107
- Community 108
- Community 109
- Community 110
- Community 111
- Community 112
- Community 113
- Community 114
- Community 115
- Community 116
- Community 117
- Community 118
- Community 119
- Community 120
- Community 121
- Community 122
- Community 123
- Community 124
- Community 125
- Community 126
- Community 127
- Community 128
- Community 129
- Community 130
- Community 131
- Community 132
- Community 133
- Community 134
- Community 135
- Community 136
- Community 137
- Community 138
- Community 139
- Community 140
- Community 141
- Community 142
- Community 143
- Community 144
- Community 145
- Community 146
- Community 147
- Community 148
- Community 149
- Community 150
- Community 151
- Community 152
- Community 153
- Community 154
- Community 155
- Community 156
- Community 157
- Community 158
- Community 159
- Community 160
- Community 162

## God Nodes (most connected - your core abstractions)
1. `GeoPoint` - 74 edges
2. `RouteHelperRepository` - 44 edges
3. `RouteHelperDao` - 41 edges
4. `IsaacsHubScaffold()` - 36 edges
5. `RoutePlayerViewModel` - 29 edges
6. `RoutedStopEntity` - 27 edges
7. `ActivityMapperApiClient` - 24 edges
8. `FeaturePromptEntity` - 23 edges
9. `RouteEntity` - 22 edges
10. `ActivityMapperRepository` - 21 edges

## Surprising Connections (you probably didn't know these)
- `toSleepEntry()` --references--> `SleepEntry`  [EXTRACTED]
  app/src/main/java/com/isaacshub/app/sleep/data/SleepRepository.kt → sleepcore/src/main/kotlin/com/isaacshub/sleep/core/SleepDebt.kt
- `ActivityMapperHomeScreen()` --calls--> `ActivityMapperApiClient`  [INFERRED]
  app/src/main/java/com/isaacshub/app/activitymapper/ui/ActivityMapperHomeScreen.kt → app/src/main/java/com/isaacshub/app/activitymapper/data/ActivityMapperApiClient.kt
- `EditRuleScreen()` --calls--> `AutomationRule`  [INFERRED]
  app/src/main/java/com/isaacshub/app/activitymapper/ui/EditRuleScreen.kt → app/src/main/java/com/isaacshub/app/activitymapper/data/ActivityMapperModels.kt
- `ActivityMapperHomeScreen()` --calls--> `ActivityMapperRepository`  [INFERRED]
  app/src/main/java/com/isaacshub/app/activitymapper/ui/ActivityMapperHomeScreen.kt → app/src/main/java/com/isaacshub/app/activitymapper/data/ActivityMapperRepository.kt
- `EditRichPresenceProfileScreen()` --calls--> `ActivityMapperRepository`  [INFERRED]
  app/src/main/java/com/isaacshub/app/activitymapper/ui/EditRichPresenceProfileScreen.kt → app/src/main/java/com/isaacshub/app/activitymapper/data/ActivityMapperRepository.kt

## Import Cycles
- None detected.

## Communities (172 total, 24 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.05
Nodes (43): AddressParity, BOTH, EVEN, ODD, UNKNOWN, HouseNumberRange, interpolateAddresses(), interpolateAddressGroups() (+35 more)

### Community 1 - "Community 1"
Cohesion: 0.05
Nodes (31): EssentialsApi, EssentialsEndpointPaths, Result, Chore, CreateChoreRequest, UpdateChoreRequest, AdminOverrideRequest, ChoreCompletion (+23 more)

### Community 2 - "Community 2"
Cohesion: 0.05
Nodes (26): AppDataBackupWorker, CoroutineWorker, Result, RoomDatabase, Preferences, preferencesToJson(), VaultApiClient, MediaItem (+18 more)

### Community 3 - "Community 3"
Cohesion: 0.05
Nodes (22): ChoreDao, Flow, CompletionDao, Flow, DayOfWeekListConverter, LocalChoreEntity, CompletionStatus, COMPLETED (+14 more)

### Community 4 - "Community 4"
Cohesion: 0.08
Nodes (23): IBinder, Intent, Service, SharedPreferences, SleepDetectionService, SensorManager, DetectionConfig, DetectionEvent (+15 more)

### Community 5 - "Community 5"
Cohesion: 0.07
Nodes (19): ApiException, ChildAccountDto, ChoreDto, EssentialsApiClient, JSONObject, Result, T, NominatimGeocoder (+11 more)

### Community 6 - "Community 6"
Cohesion: 0.07
Nodes (44): Modifier, LiveMap(), RouteBuilderScreen(), AddressActionCard(), SideButton(), Context, newOsmMapView(), ImageProxy (+36 more)

### Community 7 - "Community 7"
Cohesion: 0.06
Nodes (24): CandidateAddress, T, nearestAddresses(), nextOrNull(), stickyNearestSlots(), StopSide, IN_DRIVE, LEFT (+16 more)

### Community 8 - "Community 8"
Cohesion: 0.10
Nodes (24): extractAddress(), ExtractedAddress, extractFromDirectionSheet(), isValidStreetAddress(), AddressCameraScanner(), AddressListItem(), AmazonRouteScannerScreen(), com (+16 more)

### Community 9 - "Community 9"
Cohesion: 0.08
Nodes (18): ScheduleType, BIWEEKLY, DAILY_MON_FRI, DAILY_SAT_FRI, NONE, WEEKLY, ScheduleTypeConverter, AnchorDatePickerDialog() (+10 more)

### Community 10 - "Community 10"
Cohesion: 0.11
Nodes (9): parseScannedAddresses(), ScannedAddress, AndroidViewModel, ScannedAddress, StateFlow, MailScanUiState, MailScanViewModel, ResolvedMailStop (+1 more)

### Community 11 - "Community 11"
Cohesion: 0.11
Nodes (14): View, OverlayTouchListener, IBinder, Intent, Notification, Service, View, WindowManager (+6 more)

### Community 12 - "Community 12"
Cohesion: 0.09
Nodes (11): CreateChoreUiState, CreateChoreViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider, DownloadManager (+3 more)

### Community 13 - "Community 13"
Cohesion: 0.12
Nodes (3): PackageEntity, Flow, RouteHelperRepository

### Community 14 - "Community 14"
Cohesion: 0.11
Nodes (3): Flow, RouteHelperDao, CandidateAddressEntity

### Community 15 - "Community 15"
Cohesion: 0.12
Nodes (9): LocationSample, advanceToNextStop(), resolveMapBearing(), Context, Flow, liveLocationFlow(), toSample(), LocationSampleTest (+1 more)

### Community 16 - "Community 16"
Cohesion: 0.12
Nodes (16): EnergyForecastChart(), formatHourOfDay(), Modifier, Modifier, SleepDebtRing(), formatDuration(), formatHourOfDay(), formatRange() (+8 more)

### Community 17 - "Community 17"
Cohesion: 0.13
Nodes (6): BankAccountDao, Flow, BankAccountEntity, fromDomain(), BankingDao, Flow

### Community 18 - "Community 18"
Cohesion: 0.12
Nodes (8): BankingDatabase, getInstance(), Context, RoomDatabase, BudgetAccountSelectionEntity, BudgetCategoryEntity, BudgetDao, Flow

### Community 19 - "Community 19"
Cohesion: 0.11
Nodes (10): Flow, RouteScheduleOverrideDao, RouteScheduleOverrideEntity, currentPayPeriodRange(), ClosedRange, computePayPeriodSummary(), PayPeriodSummary, ClosedRange (+2 more)

### Community 20 - "Community 20"
Cohesion: 0.13
Nodes (5): Flow, TimeEntryDao, TimeEntryEntity, Flow, TimeTrackingRepository

### Community 21 - "Community 21"
Cohesion: 0.12
Nodes (13): ChoreCard(), CompletionProgressCard(), HomeScreen(), Modifier, ChoreUiState, Factory, HomeUiState, HomeViewModel (+5 more)

### Community 22 - "Community 22"
Cohesion: 0.19
Nodes (9): BaseApiClient, HttpURLConnection, JSONObject, MultiUrlApiClient, Failed, FeatureFunnelApiClient, LimitHit, SendPromptResult (+1 more)

### Community 23 - "Community 23"
Cohesion: 0.15
Nodes (7): RoutedStopEntity, AddSectionDialog(), RouteEditScreen(), SectionRow(), StopRow(), AmazonLoadTruckScreen(), LoadTruckStopItem()

### Community 24 - "Community 24"
Cohesion: 0.16
Nodes (10): AppBlockingService, Context, IBinder, Intent, Notification, Service, View, WindowManager (+2 more)

### Community 25 - "Community 25"
Cohesion: 0.22
Nodes (8): ActivityMapperApiClient, JSONObject, Condition, DiscordRichPresenceProfile, EditRichPresenceProfileScreen(), ActionEditor(), ConditionEditor(), EditRuleScreen()

### Community 26 - "Community 26"
Cohesion: 0.16
Nodes (10): Context, NapAlarmController, Context, SharedPreferences, NapPhase, IDLE, NAPPING, RINGING (+2 more)

### Community 27 - "Community 27"
Cohesion: 0.22
Nodes (7): Result, BankingApiClient, Result, PlaidAccessToken, PlaidLinkToken, T, Exception

### Community 28 - "Community 28"
Cohesion: 0.14
Nodes (9): Flow, RouteDao, RouteEntity, Factory, StateFlow, T, ViewModel, ViewModelProvider (+1 more)

### Community 29 - "Community 29"
Cohesion: 0.13
Nodes (16): Action, ActionType, SET_DISCORD_RICH_PRESENCE, ActivityMapperData, ComparisonOperator, EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL (+8 more)

### Community 30 - "Community 30"
Cohesion: 0.27
Nodes (4): AutomationRule, ActivityMapperRepository, Result, StateFlow

### Community 31 - "Community 31"
Cohesion: 0.13
Nodes (13): CreatingLinkToken, Error, ExchangingToken, Factory, Idle, StateFlow, T, ViewModel (+5 more)

### Community 32 - "Community 32"
Cohesion: 0.17
Nodes (7): AddressFetchResult, Failure, FetchedAddress, Success, IndianaAddressPointFetcher, JSONObject, IndianaAddressPointFetcherTest

### Community 33 - "Community 33"
Cohesion: 0.14
Nodes (8): DeductionFormState, Factory, StateFlow, T, ViewModel, ViewModelProvider, TimeTrackerSettingsUiState, TimeTrackerSettingsViewModel

### Community 34 - "Community 34"
Cohesion: 0.18
Nodes (6): ChildAccountDao, Flow, ChildAccountEntity, ChildAccountCard(), ChildAccountDialog(), ManageFamilyScreen()

### Community 35 - "Community 35"
Cohesion: 0.18
Nodes (3): FeaturePromptDao, Flow, FeaturePromptEntity

### Community 36 - "Community 36"
Cohesion: 0.20
Nodes (9): Context, IBinder, Intent, Service, NapAlarmService, start(), stop(), MediaPlayer (+1 more)

### Community 37 - "Community 37"
Cohesion: 0.13
Nodes (7): EditTimeEntryUiState, EditTimeEntryViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 38 - "Community 38"
Cohesion: 0.16
Nodes (5): BankConnectionDao, Flow, BankConnectionEntity, fromDomain(), BankConnection

### Community 39 - "Community 39"
Cohesion: 0.14
Nodes (5): Keys, Flow, Preferences, UserPreferences, UserPreferencesRepository

### Community 40 - "Community 40"
Cohesion: 0.14
Nodes (7): Factory, FeatureFunnelHomeViewModel, FeatureFunnelUiState, StateFlow, T, ViewModel, ViewModelProvider

### Community 42 - "Community 42"
Cohesion: 0.26
Nodes (9): computeCarryoverHours(), computeWeeklySummary(), currentWeekRange(), ClosedRange, localDate(), WeeklySummary, weekStartFor(), hourlyEntry() (+1 more)

### Community 43 - "Community 43"
Cohesion: 0.17
Nodes (10): Modifier, PayPeriodSummaryCard(), TimeEntryRow(), Modifier, WeeklyHoursBar(), formatCurrency(), formatNumber(), DeductionRow() (+2 more)

### Community 44 - "Community 44"
Cohesion: 0.14
Nodes (9): Factory, Context, StateFlow, T, Uri, ViewModel, ViewModelProvider, PhotoCaptureUiState (+1 more)

### Community 45 - "Community 45"
Cohesion: 0.15
Nodes (10): CompletionStatus, COMPLETED, FAILED, IN_PROGRESS, NOT_STARTED, PENDING_VERIFICATION, REJECTED, VERIFIED (+2 more)

### Community 47 - "Community 47"
Cohesion: 0.16
Nodes (7): FeatureFunnelPreferences, FeatureFunnelPreferencesRepository, Keys, Flow, FeatureFunnelWorker, CoroutineWorker, Result

### Community 48 - "Community 48"
Cohesion: 0.18
Nodes (5): RouteHelperRouteEntity, AndroidViewModel, StateFlow, NewRouteUiState, RouteHelperHomeViewModel

### Community 49 - "Community 49"
Cohesion: 0.16
Nodes (10): RestoreScreen(), BackupDatabase, Error, Idle, StateFlow, ViewModel, Loading, RestoreState (+2 more)

### Community 50 - "Community 50"
Cohesion: 0.20
Nodes (6): AuthRepository, AuthToken, Result, EssentialsApp, Application, EssentialsRepository

### Community 51 - "Community 51"
Cohesion: 0.15
Nodes (7): ActivityMapperUiState, ActivityMapperViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 52 - "Community 52"
Cohesion: 0.26
Nodes (8): Flow, BankAccount, AccountCard(), AccountsList(), BankingHomeScreen(), EmptyState(), com, NumberFormat

### Community 53 - "Community 53"
Cohesion: 0.18
Nodes (8): BudgetCategory, toDomain(), toEntity(), AccountSelectionDialog(), CategoryEditDialog(), BudgetCategoryConfigCard(), BudgetConfigScreen(), NumberFormat

### Community 54 - "Community 54"
Cohesion: 0.16
Nodes (7): BudgetConfigUiState, BudgetConfigViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 55 - "Community 55"
Cohesion: 0.16
Nodes (10): BankingHomeViewModel, BankingUiState, Error, Factory, StateFlow, T, ViewModel, ViewModelProvider (+2 more)

### Community 57 - "Community 57"
Cohesion: 0.18
Nodes (7): EditPromptUiState, EditPromptViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 58 - "Community 58"
Cohesion: 0.22
Nodes (3): BankingRepository, Result, PlaidConnectionSetupScreen()

### Community 59 - "Community 59"
Cohesion: 0.26
Nodes (3): ChoreCompletionDao, Flow, ChoreCompletionEntity

### Community 60 - "Community 60"
Cohesion: 0.18
Nodes (8): Factory, StateFlow, T, ViewModel, ViewModelProvider, LandingUiState, LandingViewModel, ToolCardData

### Community 61 - "Community 61"
Cohesion: 0.15
Nodes (5): CachedRoadRouteEntity, PackageWithSequence, RouteType, AMAZON, REGULAR

### Community 62 - "Community 62"
Cohesion: 0.18
Nodes (11): DebugBridgeUiState, Error, Factory, Idle, StateFlow, T, ViewModel, ViewModelProvider (+3 more)

### Community 63 - "Community 63"
Cohesion: 0.18
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, WeekScheduleUiState, WeekScheduleViewModel

### Community 65 - "Community 65"
Cohesion: 0.23
Nodes (3): ChoreDao, Flow, ChoreEntity

### Community 66 - "Community 66"
Cohesion: 0.23
Nodes (9): CreateChoreScreen(), EditPromptScreen(), bottomDestinationsFor(), IsaacsHubApp(), IsaacsHubScaffold(), Modifier, NavHostController, navigateToTab() (+1 more)

### Community 67 - "Community 67"
Cohesion: 0.20
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, ManageFamilyUiState, ManageFamilyViewModel

### Community 68 - "Community 68"
Cohesion: 0.24
Nodes (3): toSleepEntry(), SleepEntry, SleepDebtCalculatorTest

### Community 69 - "Community 69"
Cohesion: 0.20
Nodes (7): EditSessionUiState, EditSessionViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 70 - "Community 70"
Cohesion: 0.24
Nodes (4): AndroidViewModel, StateFlow, NapUiState, NapViewModel

### Community 71 - "Community 71"
Cohesion: 0.18
Nodes (6): Factory, StateFlow, T, ViewModel, ViewModelProvider, SettingsViewModel

### Community 72 - "Community 72"
Cohesion: 0.21
Nodes (5): AndroidViewModel, StateFlow, Uri, VaultHomeUiState, VaultHomeViewModel

### Community 73 - "Community 73"
Cohesion: 0.27
Nodes (8): ChoreDto, ChoresResponse, CompletionSyncRequest, CompletionSyncResponse, EssentialsApiClient, Result, PhotoVerificationRequest, PhotoVerificationResponse

### Community 74 - "Community 74"
Cohesion: 0.21
Nodes (7): ChoreDetailUiState, ChoreDetailViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 75 - "Community 75"
Cohesion: 0.20
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, LoginUiState, LoginViewModel

### Community 76 - "Community 76"
Cohesion: 0.22
Nodes (8): VariableType, CURRENT_STOP_NUMBER, ROUTE_COMPLETION_PERCENT, ROUTE_PLAY_MODE_ACTIVE, SLEEP_TIME_REMAINING, SLEEPING, TOTAL_STOPS, VariableValue

### Community 77 - "Community 77"
Cohesion: 0.25
Nodes (7): BudgetState, calculateBudgetState(), CategoryFillState, BudgetCategoryBar(), Modifier, BudgetTowerCard(), Modifier

### Community 78 - "Community 78"
Cohesion: 0.44
Nodes (3): JSONObject, Result, PlaidClient

### Community 79 - "Community 79"
Cohesion: 0.22
Nodes (3): DeductionDao, Flow, DeductionEntity

### Community 80 - "Community 80"
Cohesion: 0.22
Nodes (7): PromptStatus, COMPLETED, FAILED, IN_PROGRESS, PAUSED, QUEUED, PromptStatusConverter

### Community 82 - "Community 82"
Cohesion: 0.38
Nodes (8): beInt32(), ByteArray, leDouble(), leInt16(), leInt32(), TigerDbfParser, TigerRawRecord, TigerShpParser

### Community 83 - "Community 83"
Cohesion: 0.20
Nodes (4): AndroidViewModel, StateFlow, RouteEditUiState, RouteEditViewModel

### Community 84 - "Community 84"
Cohesion: 0.33
Nodes (3): Flow, SleepRepository, SleepSessionEntity

### Community 85 - "Community 85"
Cohesion: 0.24
Nodes (7): getInstance(), insertDefaultDeductions(), Context, RoomDatabase, SupportSQLiteDatabase, migrate(), TimeTrackingDatabase

### Community 86 - "Community 86"
Cohesion: 0.35
Nodes (3): parsePairingPayload(), VaultConnection, VaultConnectionTest

### Community 87 - "Community 87"
Cohesion: 0.18
Nodes (7): Bundle, ComponentActivity, MainActivity, EssentialsTheme(), Modifier, UpdateUiState, UpdateBanner()

### Community 88 - "Community 88"
Cohesion: 0.22
Nodes (6): getInstance(), insertDefaultDeductions(), Context, RoomDatabase, SupportSQLiteDatabase, WorkDatabase

### Community 89 - "Community 89"
Cohesion: 0.24
Nodes (7): Factory, StateFlow, T, ViewModel, ViewModelProvider, TimeTrackingHomeUiState, TimeTrackingHomeViewModel

### Community 90 - "Community 90"
Cohesion: 0.20
Nodes (3): Keys, Flow, VaultPreferencesRepository

### Community 91 - "Community 91"
Cohesion: 0.40
Nodes (4): ChargingMonitorReceiver, BroadcastReceiver, Context, Intent

### Community 92 - "Community 92"
Cohesion: 0.20
Nodes (7): ChoreDetail, Home, Login, PhotoCapture, Routes, Settings, Setup

### Community 93 - "Community 93"
Cohesion: 0.25
Nodes (3): Activity, DeviceAdminManager, SettingsScreen()

### Community 94 - "Community 94"
Cohesion: 0.25
Nodes (4): App, Application, EssentialsRepository, RouteHelperAddressFetcher

### Community 95 - "Community 95"
Cohesion: 0.25
Nodes (4): AppDatabase, getInstance(), Context, RoomDatabase

### Community 96 - "Community 96"
Cohesion: 0.42
Nodes (4): DiscordRichPresenceManager, getInstance(), Context, JSONObject

### Community 97 - "Community 97"
Cohesion: 0.25
Nodes (5): EssentialsDatabase, getInstance(), ChoreDao, Context, RoomDatabase

### Community 98 - "Community 98"
Cohesion: 0.25
Nodes (6): EssentialsAdminViewModel, Factory, StateFlow, T, ViewModel, ViewModelProvider

### Community 99 - "Community 99"
Cohesion: 0.33
Nodes (4): CreateRouteResult, Failure, ScannedAddressData, Success

### Community 100 - "Community 100"
Cohesion: 0.36
Nodes (5): AlreadyRunning, DebugTriggerResult, Failed, SettingsApiClient, Started

### Community 102 - "Community 102"
Cohesion: 0.25
Nodes (6): Factory, HistoryViewModel, StateFlow, T, ViewModel, ViewModelProvider

### Community 103 - "Community 103"
Cohesion: 0.28
Nodes (7): Factory, HomeUiState, HomeViewModel, StateFlow, T, ViewModel, ViewModelProvider

### Community 104 - "Community 104"
Cohesion: 0.39
Nodes (7): computeWeekSchedule(), ScheduledDay, ScheduledRouteStatus, AddScheduleOverrideDialog(), DayCard(), RouteStatusRow(), WeekScheduleScreen()

### Community 105 - "Community 105"
Cohesion: 0.25
Nodes (6): currentVersionCode(), AndroidViewModel, Context, StateFlow, UpdateUiState, UpdateViewModel

### Community 106 - "Community 106"
Cohesion: 0.25
Nodes (6): currentVersionCode(), AndroidViewModel, Context, StateFlow, UpdateUiState, UpdateViewModel

### Community 107 - "Community 107"
Cohesion: 0.39
Nodes (7): ActivityMapperHomeScreen(), androidx, Modifier, RichPresenceProfileCard(), RuleCard(), SectionHeader(), VariableCard()

### Community 108 - "Community 108"
Cohesion: 0.25
Nodes (7): AccountType, CHECKING, CREDIT_CARD, INVESTMENT, LOAN, OTHER, SAVINGS

### Community 109 - "Community 109"
Cohesion: 0.29
Nodes (6): getInstance(), Context, RoomDatabase, SupportSQLiteDatabase, migrate(), RouteHelperDatabase

### Community 110 - "Community 110"
Cohesion: 0.32
Nodes (5): SleepSource, AUTO_DETECTED, MANUAL, NAP, SleepSourceConverter

### Community 111 - "Community 111"
Cohesion: 0.32
Nodes (4): computeDebt(), NightlyDebt, SleepDebtCalculator, SleepDebtResult

### Community 112 - "Community 112"
Cohesion: 0.46
Nodes (3): Context, NapAlarmScheduler, PendingIntent

### Community 113 - "Community 113"
Cohesion: 0.32
Nodes (4): DeductionType, FLAT, PERCENT, DeductionTypeConverter

### Community 114 - "Community 114"
Cohesion: 0.32
Nodes (4): PayType, EVALUATION, HOURLY, PayTypeConverter

### Community 115 - "Community 115"
Cohesion: 0.43
Nodes (5): Failure, ReleaseInfo, Success, UpdateChecker, UpdateCheckResult

### Community 116 - "Community 116"
Cohesion: 0.43
Nodes (7): formatLastRun(), isGranted(), android, LoadingButtonContent(), mediaLocationPermission(), mediaReadPermissions(), VaultHomeScreen()

### Community 117 - "Community 117"
Cohesion: 0.39
Nodes (4): DeviceAdminReceiver, EssentialsDeviceAdminReceiver, Context, Intent

### Community 118 - "Community 118"
Cohesion: 0.46
Nodes (7): CameraPermissionStep(), DeviceAdminStep(), androidx, OverlayPermissionStep(), PermissionStep(), SetupScreen(), WelcomeStep()

### Community 119 - "Community 119"
Cohesion: 0.43
Nodes (5): Failure, ReleaseInfo, Success, UpdateChecker, UpdateCheckResult

### Community 122 - "Community 122"
Cohesion: 0.29
Nodes (4): Bundle, ComponentActivity, MainActivity, IsaacsHubTheme()

### Community 123 - "Community 123"
Cohesion: 0.48
Nodes (3): Context, Notification, SleepNotifications

### Community 124 - "Community 124"
Cohesion: 0.43
Nodes (3): Context, Notification, NapNotifications

### Community 125 - "Community 125"
Cohesion: 0.33
Nodes (4): Context, HttpURLConnection, ReleaseInfo, UpdateInstaller

### Community 128 - "Community 128"
Cohesion: 0.29
Nodes (4): ChoreDetailScreen(), LoginScreen(), EssentialsNavGraph(), NavHostController

### Community 129 - "Community 129"
Cohesion: 0.33
Nodes (4): Context, HttpURLConnection, ReleaseInfo, UpdateInstaller

### Community 132 - "Community 132"
Cohesion: 0.33
Nodes (5): BankProvider, MANUAL, PLAID, SIMPLEFIN, TELLER

### Community 134 - "Community 134"
Cohesion: 0.40
Nodes (4): FeatureFunnelDatabase, getInstance(), Context, RoomDatabase

### Community 135 - "Community 135"
Cohesion: 0.33
Nodes (3): Flow, LandingPreferencesRepository, PreferencesKeys

### Community 136 - "Community 136"
Cohesion: 0.40
Nodes (4): getInstance(), Context, RoomDatabase, SleepDatabase

### Community 137 - "Community 137"
Cohesion: 0.33
Nodes (4): BootCompletedReceiver, BroadcastReceiver, Context, Intent

### Community 138 - "Community 138"
Cohesion: 0.33
Nodes (4): BroadcastReceiver, Context, Intent, NapAlarmReceiver

### Community 139 - "Community 139"
Cohesion: 0.47
Nodes (5): CameraPreview(), ImageProxy, PairingScreen(), processImageProxy(), BarcodeScanner

### Community 140 - "Community 140"
Cohesion: 0.40
Nodes (4): AndroidViewModel, StateFlow, PairingUiState, PairingViewModel

### Community 142 - "Community 142"
Cohesion: 0.53
Nodes (5): CameraPreviewScreen(), Modifier, Uri, PhotoCaptureScreen(), PhotoPreviewScreen()

### Community 144 - "Community 144"
Cohesion: 0.60
Nodes (4): LandingCard, LandingScreen(), ToolCard(), ImageVector

### Community 147 - "Community 147"
Cohesion: 0.40
Nodes (3): CoroutineWorker, Result, PackageCleanupWorker

### Community 149 - "Community 149"
Cohesion: 0.70
Nodes (4): EditTimeEntryScreen(), EntryDatePickerDialog(), RouteSelector(), TimePickerDialog()

### Community 151 - "Community 151"
Cohesion: 0.67
Nodes (3): ChoreCard(), EssentialsAdminHome(), com

### Community 152 - "Community 152"
Cohesion: 0.83
Nodes (3): EditSessionScreen(), formatInstant(), InstantPickerDialog()

### Community 153 - "Community 153"
Cohesion: 0.83
Nodes (3): formatDuration(), formatEpochMillis(), NapScreen()

### Community 154 - "Community 154"
Cohesion: 0.83
Nodes (3): formatMinutesOfDay(), SettingsScreen(), WakeTimePickerDialog()

### Community 155 - "Community 155"
Cohesion: 0.50
Nodes (3): Modifier, UpdateUiState, UpdateBanner()

### Community 156 - "Community 156"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **124 isolated node(s):** `ROUTE_PLAY_MODE_ACTIVE`, `ROUTE_COMPLETION_PERCENT`, `SLEEP_TIME_REMAINING`, `SLEEPING`, `CURRENT_STOP_NUMBER` (+119 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **24 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `IsaacsHubScaffold()` connect `Community 66` to `Community 6`, `Community 9`, `Community 11`, `Community 139`, `Community 144`, `Community 16`, `Community 149`, `Community 23`, `Community 151`, `Community 153`, `Community 152`, `Community 25`, `Community 157`, `Community 158`, `Community 159`, `Community 34`, `Community 43`, `Community 49`, `Community 52`, `Community 58`, `Community 104`, `Community 107`, `Community 116`?**
  _High betweenness centrality (0.299) - this node is a cross-community bridge._
- **Why does `GeoPoint` connect `Community 0` to `Community 32`, `Community 64`, `Community 5`, `Community 6`, `Community 7`, `Community 10`, `Community 15`, `Community 82`, `Community 23`, `Community 61`, `Community 94`?**
  _High betweenness centrality (0.143) - this node is a cross-community bridge._
- **Why does `ChildAccountEntity` connect `Community 34` to `Community 98`, `Community 67`, `Community 12`, `Community 46`?**
  _High betweenness centrality (0.094) - this node is a cross-community bridge._
- **Are the 16 inferred relationships involving `GeoPoint` (e.g. with `toSample()` and `.queryPage()`) actually correct?**
  _`GeoPoint` has 16 INFERRED edges - model-reasoned connections that need verification._
- **Are the 30 inferred relationships involving `IsaacsHubScaffold()` (e.g. with `ActivityMapperHomeScreen()` and `EditRichPresenceProfileScreen()`) actually correct?**
  _`IsaacsHubScaffold()` has 30 INFERRED edges - model-reasoned connections that need verification._
- **What connects `ROUTE_PLAY_MODE_ACTIVE`, `ROUTE_COMPLETION_PERCENT`, `SLEEP_TIME_REMAINING` to the rest of the system?**
  _124 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.05219072164948454 - nodes in this community are weakly interconnected._