# 📱 Pokedex App

![Showcase](https://raw.githubusercontent.com/vpmattei/pokedex-app/main/Showcase/Final%20Export/MainPhotoPokedexShowcase.png)

Wanted to have your own Pokedex to check your favorite Poékmons and their stats? Well now you can become the best Pokémon trainer by having your own Pokedex!

A simple but modern Pokémon Pokedex app built with **Jetpack Compose**, **Hilt for Dependency Injection**, and **Coil for Image Loading**!
This app fetches Pokémon data from the PokéAPI and supports paginated scrolling, cached Pokémon lists, and smooth navigation between each Pokémon.

## 🚀 Features
- 🔍 Search Pokémon by name
- 📜 Paginated list of Pokémons
- 🎨 Jetpack Compose UI with Material3

## 🛠️ Installation Instructions
### 1️⃣ Clone the Repository
To get started, clone this repository using **Git**:
```sh
git clone https://github.com/vpmattei/pokedex-app.git
```
Navigate into the project directory:
```sh
cd pokedex-app
```

### 2️⃣ Open the Project in Android Studio
- Open **Android Studio** (latest version recommended).
- Click on **File > Open** and select the cloned repository folder.
- Let **Gradle sync** and install dependencies automatically.

### 3️⃣ Install Dependencies
If Gradle sync fails, manually refresh dependencies by running:
```sh
./gradlew build --refresh-dependencies
```
For Windows:
```sh
gradlew.bat build --refresh-dependencies
```

### 4️⃣ Run the App
- **Connect an Android device** (or start an Emulator).
- Click on **Run > Run 'app'** (or press `Shift + F10`).
- The app should now launch on your device/emulator.

## 📦 Project Structure
```
📂 PokedexApp
 ┣ 📂 app/src/main
 ┃ ┣ 📂 java/com/example/pokedexapp
 ┃ ┃ ┣ 📂 data                # Data layer (API, models, repository)
 ┃ ┃ ┣ 📂 di                  # Dependency Injection (Hilt)
 ┃ ┃ ┣ 📂 pokemonlist         # Pokémon List UI & ViewModel
 ┃ ┃ ┣ 📂 pokemondetail       # Pokémon Detail Screen UI & ViewModel
 ┃ ┃ ┣ 📂 util                # Utility classes & helpers
 ┃ ┃ ┣ 📜 MainActivity.kt     # App entry point
 ┃ ┣ 📂 res                   # Resources (XML, images)
 ┃ ┣ 📜 AndroidManifest.xml   # Manifest file
 ┣ 📜 build.gradle.kts        # Project-level Gradle file
 ┣ 📜 settings.gradle.kts     # Settings file
 ┣ 📜 README.md               # Project Documentation (this file)
```

## 📡 API Used
This project fetches data from the **PokéAPI**:
🔗 [https://pokeapi.co/](https://pokeapi.co/)

## 🔧 Troubleshooting
### Gradle Build Fails?
Try running:
```sh
./gradlew clean
./gradlew build
```

### App Crashes on Launch?
- Check **Logcat** in Android Studio for errors.
- Ensure you have **minSdk = 28** and **targetSdk = 35** in `build.gradle.kts`.

## 📸 Screenshots

### Home Screen
![Home Screen](https://raw.githubusercontent.com/vpmattei/pokedex-app/main/Showcase/Final%20Export/PokemonListShowcase.png)

### Pokémon Details
![Venusaur](https://raw.githubusercontent.com/vpmattei/pokedex-app/main/Showcase/Final%20Export/VenusaurShowcase.png)
![Charizard](https://raw.githubusercontent.com/vpmattei/pokedex-app/main/Showcase/Final%20Export/CharizardShowcase.png)
![Blastoise](https://raw.githubusercontent.com/vpmattei/pokedex-app/main/Showcase/Final%20Export/BlastoiseShowcase.png)

### Search Functionality
![Search Functionality](https://raw.githubusercontent.com/vpmattei/pokedex-app/main/Showcase/Final%20Export/SeachbarShowcase.png)
