package com.projetopaz.frontend_paz

import androidx.compose.runtime.*
import com.projetopaz.frontend_paz.model.Category
import com.projetopaz.frontend_paz.model.Community
import com.projetopaz.frontend_paz.model.Product
import com.projetopaz.frontend_paz.model.Supplier
import com.projetopaz.frontend_paz.theme.PazTheme
import com.projetopaz.frontend_paz.ui.*

enum class Screen {
    Login, Register, Home,
    ProductList, ProductForm,
    CommunityList, CommunityForm,
    CategoryList, CategoryForm,
    SupplierList, SupplierForm,
    UserProfile,
    SaleConfig, Pos, SaleDetails
}

@Composable
fun App() {
    PazTheme {
        var currentScreen by remember { mutableStateOf(Screen.Login) }

        // Estados compartilhados
        var productToEdit by remember { mutableStateOf<Product?>(null) }
        var communityToEdit by remember { mutableStateOf<Community?>(null) }
        var categoryToEdit by remember { mutableStateOf<Category?>(null) }
        var supplierToEdit by remember { mutableStateOf<Supplier?>(null) }

        // Vendas
        var currentSaleCommunityId by remember { mutableStateOf<Long?>(null) }
        var currentSaleIsAuto by remember { mutableStateOf(false) }
        var selectedSale by remember { mutableStateOf<com.projetopaz.frontend_paz.model.SaleResponse?>(null) }

        // Refresh triggers
        var refreshProducts by remember { mutableStateOf(0) }
        var refreshCommunities by remember { mutableStateOf(0) }
        var refreshCategories by remember { mutableStateOf(0) }
        var refreshSuppliers by remember { mutableStateOf(0) } // <--- NOVO

        when (currentScreen) {
            Screen.Login -> LoginScreen({ currentScreen = Screen.Home }, { currentScreen = Screen.Register })
            Screen.Register -> RegisterScreen({ currentScreen = Screen.Login }, { currentScreen = Screen.Login })

            Screen.Home -> HomeScreen(
                onNavigateToProducts = { currentScreen = Screen.ProductList },
                onNavigateToCommunities = { currentScreen = Screen.CommunityList },
                onNavigateToSales = { currentScreen = Screen.SaleConfig },
                onNavigateToDetails = { sale -> selectedSale = sale; currentScreen = Screen.SaleDetails },
                onNavigateToCategories = { currentScreen = Screen.CategoryList },
                onNavigateToSuppliers = { currentScreen = Screen.SupplierList }, // <--- LIGADO
                onNavigateToProfile = { currentScreen = Screen.UserProfile }, // <--- LIGADO
                onLogout = { currentScreen = Screen.Login }
            )

            // --- VENDAS ---
            Screen.SaleConfig -> SaleConfigScreen({ currentScreen = Screen.Home }, { id, auto -> currentSaleCommunityId = id; currentSaleIsAuto = auto; currentScreen = Screen.Pos })
            Screen.Pos -> if (currentSaleCommunityId != null) PosScreen(currentSaleCommunityId!!, currentSaleIsAuto, { currentScreen = Screen.Home }, { currentScreen = Screen.Home }) else currentScreen = Screen.Home
            Screen.SaleDetails -> if (selectedSale != null) SaleDetailsScreen(selectedSale!!, { currentScreen = Screen.Home })

            // --- CRUDS ---
            Screen.ProductList -> ProductListScreen(refreshProducts, { currentScreen = Screen.Home }, { productToEdit = null; currentScreen = Screen.ProductForm }, { productToEdit = it; currentScreen = Screen.ProductForm })
            Screen.ProductForm -> ProductFormScreen(productToEdit, { currentScreen = Screen.ProductList }, { refreshProducts++; currentScreen = Screen.ProductList })

            Screen.CommunityList -> CommunityListScreen(refreshCommunities, { currentScreen = Screen.Home }, { communityToEdit = null; currentScreen = Screen.CommunityForm }, { communityToEdit = it; currentScreen = Screen.CommunityForm })
            Screen.CommunityForm -> CommunityFormScreen(communityToEdit, { currentScreen = Screen.CommunityList }, { refreshCommunities++; currentScreen = Screen.CommunityList })

            Screen.CategoryList -> CategoryListScreen(refreshCategories, { currentScreen = Screen.Home }, { categoryToEdit = null; currentScreen = Screen.CategoryForm }, { categoryToEdit = it; currentScreen = Screen.CategoryForm })
            Screen.CategoryForm -> CategoryFormScreen(categoryToEdit, { currentScreen = Screen.CategoryList }, { refreshCategories++; currentScreen = Screen.CategoryList })

            // --- NOVOS CRUDS ---
            Screen.SupplierList -> SupplierListScreen(refreshSuppliers, { currentScreen = Screen.Home }, { supplierToEdit = null; currentScreen = Screen.SupplierForm }, { supplierToEdit = it; currentScreen = Screen.SupplierForm })
            Screen.SupplierForm -> SupplierFormScreen(supplierToEdit, { currentScreen = Screen.SupplierList }, { refreshSuppliers++; currentScreen = Screen.SupplierList })

            Screen.UserProfile -> UserProfileScreen({ currentScreen = Screen.Home })
        }
    }
}