package africa.amoper.app.ui.nav

import africa.amoper.app.AppContainer
import africa.amoper.app.data.model.User
import africa.amoper.app.ui.auth.AuthViewModel
import africa.amoper.app.ui.auth.LoggedOutPrompt
import africa.amoper.app.ui.auth.LoginScreen
import africa.amoper.app.ui.auth.RegisterScreen
import africa.amoper.app.ui.auth.SessionState
import africa.amoper.app.ui.driver.DriverDashboardScreen
import africa.amoper.app.ui.driver.DriverViewModel
import africa.amoper.app.ui.home.LandingScreen
import africa.amoper.app.ui.logistics.LogisticsViewModel
import africa.amoper.app.ui.logistics.QuoteRequestScreen
import africa.amoper.app.ui.logistics.ShipmentDetailScreen
import africa.amoper.app.ui.logistics.ShipmentsScreen
import africa.amoper.app.ui.marketplace.CartScreen
import africa.amoper.app.ui.marketplace.CheckoutScreen
import africa.amoper.app.ui.marketplace.MarketplaceViewModel
import africa.amoper.app.ui.marketplace.ProductDetailScreen
import africa.amoper.app.ui.marketplace.ProductListScreen
import africa.amoper.app.ui.orders.OrderDetailScreen
import africa.amoper.app.ui.orders.OrdersScreen
import africa.amoper.app.ui.orders.OrdersViewModel
import africa.amoper.app.ui.profile.ProfileScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Guests can browse the whole marketplace (product list, product detail,
 * categories, search) with no account at all — the API already allows this.
 * A login/register prompt only appears for things that genuinely need an
 * account: cart, checkout, orders, logistics, and the driver dashboard.
 */
@Composable
fun AmoperApp(container: AppContainer) {
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.factory(container.authRepository))

    when (authViewModel.session) {
        is SessionState.Loading -> Box(Modifier) { CircularProgressIndicator() }
        else -> MainScaffold(container, authViewModel)
    }
}

@Composable
private fun MainScaffold(container: AppContainer, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val marketplaceViewModel: MarketplaceViewModel = viewModel(factory = MarketplaceViewModel.factory(container.marketplaceRepository))
    val ordersViewModel: OrdersViewModel = viewModel(factory = OrdersViewModel.factory(container.marketplaceRepository))
    val logisticsViewModel: LogisticsViewModel = viewModel(factory = LogisticsViewModel.factory(container.logisticsRepository))
    val driverViewModel: DriverViewModel = viewModel(factory = DriverViewModel.factory(container.driverRepository))

    val session = authViewModel.session
    val currentUser: User? = (session as? SessionState.LoggedIn)?.user
    val isLoggedIn = currentUser != null

    val tabs = remember(currentUser?.isDriver) {
        if (currentUser?.isDriver == true) listOf(BottomTab.Market, BottomTab.Logistics, BottomTab.Orders, BottomTab.Driver, BottomTab.Profile)
        else listOf(BottomTab.Market, BottomTab.Logistics, BottomTab.Orders, BottomTab.Profile)
    }

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            val showBottomBar = currentRoute != Routes.LANDING && currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER
            if (showBottomBar) NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tabIcon(tab), contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NavHost(navController = navController, startDestination = Routes.LANDING) {

                // ---- Website-style public landing page: visible before sign-in ----
                composable(Routes.LANDING) {
                    LandingScreen(
                        onSignIn = { navController.navigate(Routes.LOGIN) },
                        onRegister = { navController.navigate(Routes.REGISTER) },
                        onShop = { navController.navigate(Routes.PRODUCT_LIST) },
                        onLogistics = { navController.navigate(Routes.SHIPMENT_LIST) },
                        onTrack = { navController.navigate(Routes.LOGIN) },
                        onQuote = { navController.navigate(Routes.LOGIN) }
                    )
                }

                // ---- Market: open to everyone, no account needed ----
                composable(Routes.PRODUCT_LIST) {
                    ProductListScreen(
                        viewModel = marketplaceViewModel,
                        onOpenProduct = { navController.navigate(Routes.productDetail(it)) },
                        onOpenCart = {
                            if (isLoggedIn) navController.navigate(Routes.CART) else navController.navigate(Routes.LOGIN)
                        }
                    )
                }
                composable(
                    Routes.PRODUCT_DETAIL,
                    arguments = listOf(navArgument("productId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("productId") ?: 0L
                    ProductDetailScreen(
                        productId = id, viewModel = marketplaceViewModel,
                        isLoggedIn = isLoggedIn,
                        onBack = { navController.popBackStack() },
                        onGoToCart = { navController.navigate(Routes.CART) },
                        onRequireLogin = { navController.navigate(Routes.LOGIN) }
                    )
                }

                // ---- Cart / checkout: need an account ----
                composable(Routes.CART) {
                    AuthGated(isLoggedIn, navController, "Log in to view your cart and check out.") {
                        CartScreen(
                            viewModel = marketplaceViewModel,
                            onBack = { navController.popBackStack() },
                            onCheckout = { navController.navigate(Routes.CHECKOUT) }
                        )
                    }
                }
                composable(Routes.CHECKOUT) {
                    AuthGated(isLoggedIn, navController, "Log in to check out.") {
                        CheckoutScreen(
                            viewModel = marketplaceViewModel,
                            onBack = { navController.popBackStack() },
                            onOrderPlaced = { orderId ->
                                navController.navigate(Routes.orderDetail(orderId)) {
                                    popUpTo(Routes.PRODUCT_LIST)
                                }
                            }
                        )
                    }
                }

                // ---- Orders tab: needs an account ----
                composable(Routes.ORDER_LIST) {
                    AuthGated(isLoggedIn, navController, "Log in to see your orders.") {
                        OrdersScreen(viewModel = ordersViewModel, onOpenOrder = { navController.navigate(Routes.orderDetail(it)) })
                    }
                }
                composable(
                    Routes.ORDER_DETAIL,
                    arguments = listOf(navArgument("orderId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("orderId") ?: 0L
                    OrderDetailScreen(orderId = id, viewModel = ordersViewModel, onBack = { navController.popBackStack() })
                }

                // ---- Logistics tab: needs an account ----
                composable(Routes.SHIPMENT_LIST) {
                    AuthGated(isLoggedIn, navController, "Log in to request quotes and track shipments.") {
                        ShipmentsScreen(
                            viewModel = logisticsViewModel,
                            onOpenShipment = { navController.navigate(Routes.shipmentDetail(it)) },
                            onNewQuote = { navController.navigate(Routes.QUOTE_REQUEST) },
                            onTrackResult = { navController.navigate(Routes.shipmentDetail(0L) + "?tracked=1") }
                        )
                    }
                }
                composable(
                    Routes.SHIPMENT_DETAIL + "?tracked={tracked}",
                    arguments = listOf(
                        navArgument("shipmentId") { type = NavType.LongType },
                        navArgument("tracked") { type = NavType.StringType; defaultValue = "0" }
                    )
                ) { backStackEntry ->
                    val tracked = backStackEntry.arguments?.getString("tracked") == "1"
                    val id = backStackEntry.arguments?.getLong("shipmentId") ?: 0L
                    ShipmentDetailScreen(
                        shipmentId = if (tracked) null else id,
                        viewModel = logisticsViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Routes.QUOTE_REQUEST) {
                    AuthGated(isLoggedIn, navController, "Log in to request a shipping quote.") {
                        QuoteRequestScreen(
                            viewModel = logisticsViewModel,
                            onBack = { navController.popBackStack() },
                            onBooked = { shipmentId ->
                                navController.navigate(Routes.shipmentDetail(shipmentId)) { popUpTo(Routes.SHIPMENT_LIST) }
                            }
                        )
                    }
                }

                // ---- Driver tab: needs an account (tab itself is hidden unless the user is a driver) ----
                composable(BottomTab.Driver.route) {
                    AuthGated(isLoggedIn, navController, "Log in with your driver account to see your deliveries.") {
                        DriverDashboardScreen(viewModel = driverViewModel)
                    }
                }

                // ---- Profile: shows login/register for guests, the real profile once logged in ----
                composable(Routes.PROFILE) {
                    if (currentUser != null) {
                        ProfileScreen(user = currentUser, onLogout = { authViewModel.logout() })
                    } else {
                        LoggedOutPrompt(
                            message = "Log in or create an account to check out, track shipments, and more.",
                            onLogin = { navController.navigate(Routes.LOGIN) },
                            onRegister = { navController.navigate(Routes.REGISTER) }
                        )
                    }
                }

                // ---- Auth screens: reached only when a guest tries to do something that needs an account ----
                composable(Routes.LOGIN) {
                    LaunchedEffect(authViewModel.session) {
                        if (authViewModel.session is SessionState.LoggedIn) navController.popBackStack()
                    }
                    LoginScreen(viewModel = authViewModel, onGoToRegister = { navController.navigate(Routes.REGISTER) })
                }
                composable(Routes.REGISTER) {
                    LaunchedEffect(authViewModel.session) {
                        if (authViewModel.session is SessionState.LoggedIn) navController.popBackStack()
                    }
                    RegisterScreen(viewModel = authViewModel, onBackToLogin = { navController.popBackStack() })
                }
            }
        }
    }
}

/** Shows [content] once logged in; otherwise a log-in/register prompt instead of a raw API error. */
@Composable
private fun AuthGated(
    isLoggedIn: Boolean,
    navController: NavHostController,
    message: String,
    content: @Composable () -> Unit
) {
    if (isLoggedIn) {
        content()
    } else {
        LoggedOutPrompt(
            message = message,
            onLogin = { navController.navigate(Routes.LOGIN) },
            onRegister = { navController.navigate(Routes.REGISTER) }
        )
    }
}

private fun tabIcon(tab: BottomTab) = when (tab) {
    BottomTab.Market -> Icons.Filled.Storefront
    BottomTab.Logistics -> Icons.Filled.LocalShipping
    BottomTab.Orders -> Icons.Filled.ListAlt
    BottomTab.Driver -> Icons.Filled.DirectionsCar
    BottomTab.Profile -> Icons.Filled.Person
}
