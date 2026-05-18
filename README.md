# 🏠 CozySpace Frontend

**CozySpace** est une application multiplateforme moderne construite avec **Kotlin Multiplatform**. Cette application s'exécute sur **Android**, **iOS**, **Web** et **Desktop** à partir d'une base de code unique partagée.

---

## 📋 Table des matières

- [Architecture du projet](#️-architecture-du-projet)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Commandes de développement](#-commandes-de-développement)
  - [Android](#android)
  - [Desktop (JVM)](#desktop-jvm)
  - [Web (Angular)](#web-angular)
  - [iOS](#ios-macos-uniquement)
- [Linting et Qualité du code](#-linting-et-qualité-du-code)
- [Développement](#-développement)
- [Ressources utiles](#-ressources-utiles)

---

## 🏗️ Architecture du projet

Ce projet Kotlin Multiplatform est organisé en plusieurs modules :

### Dossiers principaux

| Dossier | Description |
|---------|-------------|
| **`/composeApp`** | Code partagé pour toutes les applications Compose Multiplatform (Android, Desktop, iOS) |
| **`/shared`** | Code partagé entre toutes les targets incluant le code Kotlin/JS pour le web |
| **`/webApp`** | Application web Angular (utilise la librairie Kotlin/JS du module `shared`) |
| **`/iosApp`** | Point d'entrée pour l'application iOS (SwiftUI) |
| **`/gradle`** | Configuration Gradle et versions des dépendances |

### Structure du code partagé

#### Dans `/composeApp/src`
- **`commonMain`** → Code partagé pour toutes les targets
- **`androidMain`** → Code spécifique Android
- **`iosMain`** → Code spécifique iOS
- **`jvmMain`** → Code spécifique Desktop (JVM)

#### Dans `/shared/src`
- **`commonMain`** → Code partagé entre toutes les targets
- **`jsMain`** → Code spécifique pour la compilation Kotlin/JS (web)
- **`androidMain`**, **`iosMain`**, **`jvmMain`** → Code spécifique aux platforms

---

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java Development Kit (JDK) 21**
- **Node.js** (pour les applications web et npm)
- **Xcode** (pour le développement iOS sur macOS)
- **Android Studio** ou **Android SDK** (pour Android)

> ⚠️ **Important :** Ce projet doit être exécuté avec **Java 21**.

### Configuration de `JAVA_HOME` (si plusieurs versions de Java sont installées)

Si vous avez plusieurs JDK installés, assurez-vous que `JAVA_HOME` pointe vers le **JDK 21** avant d'exécuter les commandes Gradle.

Exemples :

```bash
# macOS / Linux
export JAVA_HOME=/chemin/vers/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
java -version
./gradlew --version
```

```powershell
# Windows PowerShell
$env:JAVA_HOME="C:\\Program Files\\Java\\jdk-21"
$env:Path="$env:JAVA_HOME\\bin;$env:Path"
java -version
.\\gradlew.bat --version
```

Vous pouvez aussi le faire uniquement pour chaque commande (sans changer la configuration globale) :

```bash
JAVA_HOME=/chemin/vers/jdk-21 ./gradlew ...
```

---

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone https://github.com/MTI-Bloomy/CozySpace-Front.git
cd CozySpace-Front
```

### 2. Configuration initiale complète ⚠️

**IMPORTANT :** Pour que les Git hooks Husky se mettent en place correctement, vous **DEVEZ** exécuter ces étapes dans cet ordre :

```bash
# 1. Compiler le code Kotlin/JS partagé
# Windows
.\gradlew.bat :shared:jsBrowserDevelopmentLibraryDistribution

# macOS/Linux
./gradlew :shared:jsBrowserDevelopmentLibraryDistribution

# 2. Installer les dépendances npm (installe aussi Husky et les hooks)
npm install
```

**Pourquoi c'est important :**
- Le build Gradle web est nécessaire pour que le linting Angular fonctionne dans les pre-commit hooks
- `npm install` active les Git hooks Husky (script `prepare` dans package.json)
- **Sans ces deux étapes, les vérifications pré-commit ne fonctionneront pas correctement** ⚠️

---

## 💻 Commandes de développement

### Android

**Build la version Debug :**
```bash
# Windows
.\gradlew.bat :composeApp:assembleDebug

# macOS/Linux
./gradlew :composeApp:assembleDebug
```

### Desktop (JVM)

**Build et exécute l'application Desktop :**
```bash
# Windows
.\gradlew.bat :composeApp:run

# macOS/Linux
./gradlew :composeApp:run
```

### Web (Angular)

**Étape 1 :** Compiler le code Kotlin/JS partagé
```bash
# Windows
.\gradlew.bat :shared:jsBrowserDevelopmentLibraryDistribution

# macOS/Linux
./gradlew :shared:jsBrowserDevelopmentLibraryDistribution
```

**Étape 2 :** Installer les dépendances et lancer le serveur de développement
```bash
npm install
npm run start
```

L'application sera accessible sur `http://localhost:4200`

### iOS (macOS uniquement)

**Option 1 :** Utiliser Xcode
- Ouvrir le dossier `/iosApp` dans Xcode
- Appuyer sur le bouton **Run** dans Xcode

**Option 2 :** Ligne de commande (macOS uniquement)
```bash
./gradlew :composeApp:build
```

---

## 🔍 Linting et Qualité du code

Nous utilisons deux outils de qualité de code pour maintenir une base de code propre et cohérente :

### **KTLint** - Formatage Kotlin

KTLint force un style de code uniforme dans tous les fichiers Kotlin.

**Vérifier le style de code :**
```bash
# Windows
.\gradlew.bat ktlintCheck

# macOS/Linux
./gradlew ktlintCheck
```

**Corriger automatiquement les problèmes de style :**
```bash
# Windows
.\gradlew.bat ktlintFormat

# macOS/Linux
./gradlew ktlintFormat
```

#### Configuration KTLint
- ✅ Les fichiers `**/generated/**` sont ignorés
- ✅ Les erreurs arrêtent le build (pas d'indulgence)

### **Detekt** - Analyse statique

Detekt détecte les problèmes de qualité de code et les bugs potentiels.

**Exécuter une analyse statique :**
```bash
# Windows
.\gradlew.bat detekt

# macOS/Linux
./gradlew detekt
```

**Voir le rapport HTML généré :**
Après exécution, consultez le rapport à :
```
build/reports/detekt/detekt.html
```

#### Configuration Detekt
- Utilise la configuration par défaut avec des personnalisations
- Toutes les règles recommandées sont appliquées

### **ESLint** - Linting Angular

> ⚠️ **Prérequis :** Vous devez d'abord avoir compilé le code Kotlin/JS partagé et installé les dépendances npm

**Avant de linter, assurez-vous d'avoir :**
```bash
# 1. Compiler le code Kotlin/JS partagé
# Windows
.\gradlew.bat :shared:jsBrowserDevelopmentLibraryDistribution

# macOS/Linux
./gradlew :shared:jsBrowserDevelopmentLibraryDistribution

# 2. Installer les dépendances npm
npm install
```

**Vérifier le code Angular :**
```bash
npm run lint
```

**Corriger automatiquement les problèmes :**
```bash
npm run lint:fix
```

#### Configuration ESLint
- ✅ Règles ESLint pour Angular et TypeScript
- ✅ Détecte les erreurs de syntaxe et les mauvaises pratiques

### **Build complet avec vérifications**

Pour construire l'application en s'assurant que tout le code respecte les standards :

```bash
# Windows
.\gradlew.bat build

# macOS/Linux
./gradlew build
```

Cette commande va :
- ✅ Exécuter KTLint
- ✅ Exécuter Detekt
- ✅ Compiler toutes les targets
- ✅ Exécuter les tests

---

## 💡 Développement

### Avant de commiter

**Pas besoin de faire quoi que ce soit !** 🤖

Ce projet utilise **Husky** avec des Git hooks qui s'exécutent automatiquement lors du commit :

#### Ce qui s'exécute automatiquement

**Pre-commit hook :**
```bash
# 1. Lint Angular
npm run lint || exit 1

# 2. Lint Kotlin
./gradlew ktlintCheck || exit 1
```

**Commit-msg hook :**
- Valide le format du message de commit (Commitlint)

##### Commitlint

Nous utilisons Commitlint pour valider le format des messages de commit (Conventional Commits).

Exemples de messages valides :

- `feat(auth): ajouter l'authentification JWT`
- `fix: corriger l'alignement du bouton`
- `chore(deps): mettre à jour les dépendances`

Règles principales : `type(scope): sujet` — où `type` est par exemple `feat`, `fix`, `chore`, `docs`, `style`, `refactor`, `perf`, `test`, `ci`.

Documentation et vérification locale :

- Site officiel : https://commitlint.js.org/
- Vérifier le dernier message de commit localement : `npx commitlint --edit`
- Vérifier une fourchette de commits : `npx commitlint --from=HEAD~1 --to=HEAD`

La configuration du projet se trouve dans [commitlint.config.js](commitlint.config.js) (et peut aussi être référencée depuis [package.json](package.json)).

#### ⚠️ Prérequis critiques pour que Husky fonctionne

Pour que les hooks Husky s'installent et fonctionnent correctement, vous **devez** avoir fait :

1. ✅ `./gradlew :shared:jsBrowserDevelopmentLibraryDistribution` (build Kotlin web)
2. ✅ `npm install` (installe Husky via le script `prepare`)

**Sans ces deux étapes, les vérifications pré-commit ne fonctionneront pas !**

Si quelque chose échoue :
- Le commit sera **bloqué**
- Vous devrez corriger les erreurs et recommencer
- Consultez les sections [KTLint](#ktlint---formatage-kotlin) et [ESLint](#eslint---linting-angular) pour les corrections

#### Commandes utiles pendant le développement

Si vous voulez vérifier manuellement avant de commiter :

```bash
# Formater le code Kotlin
./gradlew ktlintFormat

# Vérifier la qualité du code Kotlin
./gradlew ktlintCheck detekt

# Linter le code Angular
npm run lint:fix
```

---

## 📚 Ressources utiles

- [Documentation Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-create-first-app.html)
- [KTLint GitHub](https://github.com/pinterest/ktlint)
- [Detekt GitHub](https://github.com/detekt/detekt)
- [Angular Documentation](https://angular.io/)

---

**Créez le refuge que vous méritez !**
