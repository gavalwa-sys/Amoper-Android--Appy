package africa.amoper.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import africa.amoper.app.R
import africa.amoper.app.ui.theme.AmoperLime
import africa.amoper.app.ui.theme.AmoperNavy

private data class Service(val number: String, val title: String, val detail: String)
private data class Category(val title: String, val image: String)

private val services = listOf(
    Service("01", "Cargo transportation", "Road, air, sea & multimodal"),
    Service("02", "Freight forwarding", "Regional and international freight"),
    Service("03", "Warehousing", "Storage, sorting & consolidation"),
    Service("04", "Corporate logistics", "Supply chain built for growth"),
    Service("05", "Cross-border support", "Documents, customs and routes"),
    Service("06", "Last-mile delivery", "Reliable final-mile fulfillment")
)

private val categories = listOf(
    Category("Phones & tech", "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=500&q=80"),
    Category("Fashion", "https://images.unsplash.com/photo-1445205170230-053b83016050?auto=format&fit=crop&w=500&q=80"),
    Category("Home & living", "https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?auto=format&fit=crop&w=500&q=80"),
    Category("Beauty", "https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=500&q=80"),
    Category("Agriculture", "https://images.unsplash.com/photo-1625246333195-78d9c38ad449?auto=format&fit=crop&w=500&q=80")
)

@Composable
fun LandingScreen(
    onSignIn: () -> Unit,
    onRegister: () -> Unit,
    onShop: () -> Unit,
    onLogistics: () -> Unit,
    onTrack: () -> Unit,
    onQuote: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Color.White)
    ) {
        // Header — mirrors the PHP website's pre-login header.
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.amoper_logo),
                contentDescription = "AMOPER",
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(24.dp))
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("AMOPER", fontWeight = FontWeight.Black, fontSize = 20.sp, color = AmoperNavy)
                Text("Moving Africa. Connecting markets.", fontSize = 10.sp, color = Color.Gray)
            }
            TextButton(onClick = onSignIn) { Text("Sign in") }
        }

        // Country/currency strip.
        Row(
            Modifier.fillMaxWidth().background(Color(0xFFF3F6F0)).padding(12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Kenya · KES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("   •   ", color = Color.Gray)
            Text("Zambia · ZMW", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Search bar.
        OutlinedTextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            enabled = true,
            placeholder = { Text("Search products, brands and sellers...") },
            leadingIcon = { Icon(Icons.Filled.Search, null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        // Hero.
        Box(Modifier.fillMaxWidth().height(330.dp)) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.amoper_hero),
                contentDescription = "AMOPER logistics",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.fillMaxSize().background(Color(0xCC102033)))
            Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
                Text("AMOPER LOGISTICS", color = AmoperLime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Moving Africa.\nConnecting markets.", color = Color.White, fontSize = 38.sp, lineHeight = 40.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(10.dp))
                Text(
                    "Parcels, commercial cargo, freight and e-commerce orders across African roads, facilities and borders.",
                    color = Color(0xFFE3EBE9), fontSize = 13.sp, lineHeight = 19.sp
                )
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = onQuote, colors = ButtonDefaults.buttonColors(containerColor = AmoperLime, contentColor = AmoperNavy)) {
                        Text("Ship cargo →", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = onTrack, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) {
                        Text("Track")
                    }
                }
            }
        }

        // Trust row.
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Trust("✓", "Fast, reliable", "Checkout to doorstep")
            Trust("✓", "Secure by design", "Trusted movement")
        }
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Trust("⌖", "Local knowledge", "Kenya & Zambia")
            Trust("?", "Human support", "Here when needed")
        }

        SectionTitle("01 / AMOPER Logistics", "Move cargo across Africa.")
        Text(
            "From pickup to border support, warehousing and last-mile delivery, AMOPER Logistics gives every shipment a clear route forward.",
            Modifier.padding(horizontal = 20.dp), color = Color(0xFF68767A), fontSize = 13.sp, lineHeight = 20.sp
        )
        Spacer(Modifier.height(16.dp))
        services.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                row.forEach { service ->
                    Card(
                        modifier = Modifier.weight(1f).padding(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8F4)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(Modifier.padding(14.dp).fillMaxWidth()) {
                            Text(service.number, color = AmoperLime.copy(alpha = 0.9f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(service.title, fontWeight = FontWeight.Bold, color = AmoperNavy, fontSize = 13.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(service.detail, color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        // Quote CTA.
        Card(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3E9)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("PLAN YOUR MOVEMENT", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text("Get a cargo quote.", fontSize = 28.sp, fontWeight = FontWeight.Black, color = AmoperNavy)
                Spacer(Modifier.height(6.dp))
                Text("Origin • Destination • Cargo type • Weight & packages", fontSize = 12.sp, color = Color(0xFF68767A))
                Spacer(Modifier.height(14.dp))
                Button(onClick = onQuote, colors = ButtonDefaults.buttonColors(containerColor = AmoperNavy)) {
                    Text("Calculate estimate →")
                }
            }
        }

        // Market division.
        Column(
            Modifier.fillMaxWidth().background(AmoperNavy).padding(24.dp)
        ) {
            Text("02 / AMOPER MARKET", color = AmoperLime, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Shop. Buy.\nDelivered by AMOPER.", color = Color.White, fontSize = 32.sp, lineHeight = 34.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(10.dp))
            Text(
                "Discover products from sellers across Kenya and Zambia, powered by the logistics network that moves them to your door.",
                color = Color(0xFFAFBEC1), fontSize = 13.sp, lineHeight = 20.sp
            )
            Spacer(Modifier.height(18.dp))
            Button(onClick = onShop, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = AmoperNavy)) {
                Text("Shop AMOPER Market →", fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onRegister) { Text("Sell on AMOPER", color = Color.White) }
        }

        SectionTitle("BROWSE YOUR WAY", "Find your next favourite.")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { category ->
                Card(shape = RoundedCornerShape(8.dp), onClick = onShop, modifier = Modifier.width(145.dp)) {
                    Column {
                        AsyncImage(
                            model = category.image,
                            contentDescription = category.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(105.dp)
                        )
                        Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(category.title, Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Icon(Icons.Filled.ArrowForward, null, modifier = Modifier.size(15.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))
        Row(
            Modifier.fillMaxWidth().background(Color(0xFFF3F6F0)).padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("14 markets", fontSize = 22.sp, fontWeight = FontWeight.Black, color = AmoperNavy)
                Text("Local expertise with cross-border reach", fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Filled.LocalShipping, null, tint = AmoperLime, modifier = Modifier.size(40.dp))
        }

        Row(Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onSignIn, modifier = Modifier.weight(1f)) { Text("Sign in") }
            Button(onClick = onRegister, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = AmoperNavy)) { Text("Create account") }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable private fun SectionTitle(kicker: String, title: String) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
        Text(kicker, fontSize = 10.sp, color = Color(0xFF879D27), fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text(title, fontSize = 28.sp, lineHeight = 31.sp, fontWeight = FontWeight.Black, color = AmoperNavy)
    }
}

@Composable private fun Trust(icon: String, title: String, detail: String) {
    Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8F4))) {
        Column(Modifier.padding(12.dp)) {
            Text(icon, color = AmoperLime, fontWeight = FontWeight.Black)
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmoperNavy)
            Text(detail, fontSize = 9.sp, color = Color.Gray)
        }
    }
}
