/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.model.Puppy
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        AppNavigator()
    }
}
// get dummy puppies here
fun getDummyPuppies(): List<Puppy> {
    val puppyList: MutableList<Puppy> = mutableListOf()
    puppyList.add(Puppy("Brno", 2, "Very SHarp dog here with a very cute little smile in its face and sharp minded", R.drawable.dog1))
    puppyList.add(Puppy("Honey", 4, "Cute little furry puppy with very smart and addictive smile in her face", R.drawable.dog2))
    puppyList.add(Puppy("Tor", 5, "Nice little cute puppy very cheer full and play full", R.drawable.dog3))
    puppyList.add(Puppy("Sheer", 2, "Cool smart and calm nature dog", R.drawable.dog4))
    puppyList.add(Puppy("Kiley", 8, "Great sensing power can easily understand your feelings", R.drawable.dog5))
    return puppyList
}
// navigation component
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "puppyList") {
        composable("puppyList") { PuppyList(navController = navController, puppies = getDummyPuppies()) }
        composable(
            "puppyDetails/{puppy}",
            arguments = listOf(navArgument("puppy") { type = NavType.StringType })
        ) {
            it.arguments?.getString("puppy")?.let { json ->
                val puppy = Gson().fromJson(json, Puppy::class.java)
                PuppyDetails(puppy = puppy)
            }
        }
    }
}

@Composable
fun PuppyList(navController: NavController, puppies: List<Puppy>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(puppies) {
            puppy ->
            PuppyListItem(navController = navController, puppy = puppy)
        }
    }
}

@Composable
fun PuppyListItem(navController: NavController, puppy: Puppy) {
    fun navigateToPuppyDetails(puppy: Puppy) {
        val puppyJson = Gson().toJson(puppy)
        navController.navigate("puppyDetails/$puppyJson")
    }
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.clickable {
            navigateToPuppyDetails(puppy = puppy)
        },
        elevation = 2.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {

            Image(
                painter = painterResource(id = puppy.image), contentDescription = null,
                modifier = Modifier.weight(1f).height(100.dp).clip(shape = androidx.compose.foundation.shape.CircleShape),
                contentScale = ContentScale.FillBounds
            )
            Column(modifier = Modifier.weight(2f).padding(start = 4.dp).wrapContentSize(Alignment.Center)) {
                Text(text = "Name : ${puppy.name}", style = typography.h6)
                Text(text = "Type Dog", style = typography.body2)
                Text(text = "Age ${puppy.age}", style = typography.body1)
            }
        }
    }
}
@Composable
fun PuppyDetails(puppy: Puppy) {
    MaterialTheme {
        val typography = MaterialTheme.typography
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Image(
                painter = painterResource(puppy.image),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))

            Text(
                puppy.name,
                style = typography.h6
            )
            Text(
                puppy.description,
                style = typography.body2
            )
            Text(
                "${puppy.age}",
                style = typography.body2
            )
        }
    }
}
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        Surface(color = MaterialTheme.colors.background) {
            PuppyDetails(Puppy("oio", 4, "455", R.drawable.dog2))
        }
    }
}
/*
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}*/
