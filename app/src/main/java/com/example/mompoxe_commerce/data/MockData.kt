package com.example.mompoxe_commerce.data

import com.example.mompoxe_commerce.data.model.Product

object MockData {
    val productList = listOf(
        Product(
            id = 1,
            name = "Café Premium",
            description = "Granos seleccionados con tueste medio.",
            price = 5.99,
            imageUrl = "https://picsum.photos/300",
            category = "Bebidas"
        ),
        Product(
            id = 2,
            name = "Té Verde Orgánico",
            description = "100% orgánico, cosechado a mano.",
            price = 3.49,
            imageUrl = "https://picsum.photos/300",
            category = "Bebidas"
        ),
        Product(
            id = 3,
            name = "Chocolate Amargo 70%",
            description = "Tableta de 100g con cacao premium.",
            price = 4.25,
            imageUrl = "https://picsum.photos/300",
            category = "Dulces"
        ),
        Product(
            id = 4,
            name = "Miel Pura de Abeja",
            description = "500ml de miel natural sin aditivos.",
            price = 6.75,
            imageUrl = "https://picsum.photos/300",
            category = "Dulces"
        ),
        Product(
            id = 5,
            name = "Galletas Artesanales",
            description = "Paquete de 12 galletas caseras.",
            price = 2.99,
            imageUrl = "https://picsum.photos/300",
            category = "Panadería"
        )
    )
}
