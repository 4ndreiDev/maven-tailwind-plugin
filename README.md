# Maven Tailwind Plugin

## 📑 Table of Contents

- [What is the Maven Tailwind Plugin?](#what-is-the-maven-tailwind-plugin)
  - [Why Use This Plugin?](#why-use-this-plugin)
  - [Who Should Use This?](#who-should-use-this)
- [🚀 Installation](#-installation)
- [⚡ Quick Start Guide](#-quick-start-guide)
- [📖 Usage](#-usage)
  - [🔨 Command 1: Standard Compilation](#-command-1-standard-compilation)
  - [👀 Command 2: Watch Mode (Development)](#-command-2-watch-mode-development)
  - [📥 Command 3: Download Binary](#-command-3-download-binary)
- [⚙️ Configuration Parameters](#️-configuration-parameters)
- [📁 Expected Directory Structure](#-expected-directory-structure)
- [🔧 Requirements](#-requirements)
- [🌐 Supported Operating Systems](#-supported-operating-systems)
- [📚 Common Use Cases](#-common-use-cases)
- [🐛 Troubleshooting](#-troubleshooting)
- [📝 Cache Files](#-cache-files)
- [🎯 Maven Lifecycle](#-maven-lifecycle)
- [📖 Tailwind Documentation](#-tailwind-documentation)
- [👨‍💻 Author](#-author)
- [📄 License](#-license)
- [❓ Frequently Asked Questions (FAQ)](#-frequently-asked-questions-faq)
- [📞 Support](#-support)

---

## What is the Maven Tailwind Plugin?

The **Maven Tailwind Plugin** is a powerful Maven plugin that simplifies the integration of **Tailwind CSS** into your Maven-based Java projects. Instead of managing Node.js dependencies or dealing with npm packages, this plugin provides seamless Tailwind CSS compilation directly through Maven commands.

### Why Use This Plugin?

- 🚫 **Zero Node.js Dependency**: No need to install Node.js or npm in your project. The plugin automatically downloads the Tailwind CLI binary compatible with your operating system.
- 🔗 **Seamless Maven Integration**: The plugin integrates naturally with Maven's build lifecycle. Your CSS is compiled automatically during the build process without extra configuration.
- ⚡ **Fast Development Workflow**: Use watch mode to get instant CSS recompilation as you modify your templates and add Tailwind classes.
- 📦 **Production-Ready**: Automatic CSS minification for production builds, reducing file size and improving load times.
- 🌍 **Cross-Platform Support**: Works on Windows, macOS, and Linux with support for multiple architectures (x64, ARM64, ARMv7).
- 🔄 **Version Control**: Easily switch between different Tailwind CSS versions without modifying your local environment.

### Who Should Use This?

This plugin is ideal for:
- 🏗️ Maven-based projects (Spring Boot, Jakarta EE, etc.)
- 👥 Teams that prefer Maven over npm/yarn for dependency management
- 🎯 Projects where you want a single build tool (Maven) instead of multiple tools
- 🔐 Organizations with strict security policies that prefer not to use npm

---

## 🚀 Installation

### 1. Add the plugin to your `pom.xml`

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.deey</groupId>
            <artifactId>maven-tailwind-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <!-- Optional configuration (see Configuration Parameters section) -->
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 2. Create the CSS input file

Create the Tailwind CSS input file at:
```
src/main/resources/static/css/input.css
```

**Basic content of `input.css`:**
```css
@import "tailwindcss";
```

For more details on Tailwind CSS installation, see the [official Tailwind CLI documentation](https://tailwindcss.com/docs/installation/tailwind-cli).

## ⚡ Quick Start Guide

Follow these steps to get started with the Maven Tailwind Plugin in minutes:

### 1️⃣ Step 1: Create the CSS Input File

Create the directory and file:
```bash
mkdir -p src/main/resources/static/css
```

Create `src/main/resources/static/css/input.css` with the following content:
```css
@import "tailwindcss";
```

### 2️⃣ Step 2: Add the Plugin to Your Project

Open your `pom.xml` and add the plugin to the `<build>` section:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.deey</groupId>
            <artifactId>maven-tailwind-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
        </plugin>
    </plugins>
</build>
```

### 3️⃣ Step 3: Compile Tailwind CSS

Run the compilation command:
```bash
mvn tailwind:compile
```

The compiled CSS will be created at: `target/classes/static/tailwind.css`

### 4️⃣ Step 4: Use the CSS in Your HTML

Add the link to your HTML file:
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 🔑 IMPORTANT: Link to the compiled Tailwind CSS -->
    <link rel="stylesheet" href="/tailwind.css">
    <title>My Tailwind App</title>
</head>
<body>
    <div class="flex h-screen items-center justify-center bg-gradient-to-r from-blue-500 to-purple-600">
        <div class="text-center text-white">
            <h1 class="text-5xl font-bold">Welcome to Tailwind CSS</h1>
            <p class="mt-4 text-xl">Built with Maven Tailwind Plugin</p>
        </div>
    </div>
</body>
</html>
```

### 5️⃣ Step 5: Development with Watch Mode

During development, use watch mode to automatically recompile on changes:

**Terminal 1: Start your application**
```bash
mvn spring-boot:run
# or
java -jar target/my-app.jar
```

**Terminal 2: Watch for CSS changes**
```bash
mvn tailwind:watch
```

Now whenever you modify your HTML files and add Tailwind classes, the CSS will automatically recompile!

### 6️⃣ Step 6: Build for Production

When building for production, the CSS is automatically minified:
```bash
mvn clean package
```

Done! Your Tailwind CSS is now compiled and ready to use. 🎉

---

## 📖 Usage

### 🔨 Command 1: Standard Compilation

Compile your Tailwind CSS in a single pass:

```bash
mvn tailwind:compile
```

**What it does:**
- Compiles the CSS input file
- Generates `target/classes/static/tailwind.css` (minified by default)
- Automatically executes in the `generate-resources` phase of Maven lifecycle

**Example usage in the full lifecycle:**
```bash
mvn clean install
```

---

### 👀 Command 2: Watch Mode (Development)

Start Tailwind in watch mode to automatically recompile when you change your files:

```bash
mvn tailwind:watch
```

**What it does:**
- Monitors changes in your CSS/HTML files
- Automatically recompiles when changes are detected
- Press `Ctrl+C` to stop the process
- Useful during local development
- CSS is **not minified** by default in watch mode

**Enable minification in watch mode (if needed):**
```bash
mvn tailwind:watch -Dtailwind.minify=true
```

---

### 📥 Command 3: Download Binary

Download or reload the Tailwind CSS binary:

```bash
mvn tailwind:download
```

**What it does:**
- Downloads the Tailwind CLI binary compatible with your OS
- Automatically executes in the `initialize` phase
- Useful for preloading the cache

**Force download (download again):**
```bash
mvn tailwind:download -Dtailwind.forceDownload=true
```

---

## ⚙️ Configuration Parameters

Customize the plugin behavior using properties in your `pom.xml` or from the command line.

| Parameter | Maven Property | Default Value | Description |
|-----------|-----------------|-------------------|-------------|
| `inputFile` | `tailwind.inputFile` | `${project.basedir}/src/main/resources/static/css/input.css` | Path to the CSS input file |
| `outputFile` | `tailwind.outputFile` | `${project.build.outputDirectory}/static/tailwind.css` | Path to the compiled CSS file |
| `minify` | `tailwind.minify` | `true` (compile), `false` (watch) | Minify CSS in compilation |
| `version` | `tailwind.version` | `v4.1.18` | Version of Tailwind CSS to use. See [available versions](https://github.com/tailwindlabs/tailwindcss/releases/) |
| `downloadDirectory` | `tailwind.downloadDirectory` | `${user.home}/.m2/tailwindcss` | Directory where the binary is cached |
| `baseUrl` | `tailwind.baseUrl` | `https://github.com/tailwindlabs/tailwindcss/releases/download` | Base URL for downloading the binary |
| `forceDownload` | `tailwind.forceDownload` | `false` | Force binary download (ignore cache) |

### Configuration Examples

**Example 1: Customize in `pom.xml`**

```xml
<plugin>
    <groupId>com.deey</groupId>
    <artifactId>maven-tailwind-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <inputFile>${project.basedir}/src/main/resources/css/styles.css</inputFile>
        <outputFile>${project.build.outputDirectory}/css/output.css</outputFile>
        <minify>true</minify>
        <version>v4.1.18</version>
    </configuration>
</plugin>
```

**Example 2: Pass parameters from command line**

```bash
# Compile with custom input file
mvn tailwind:compile -Dtailwind.inputFile=src/main/resources/css/input.css

# Watch without minification
mvn tailwind:watch -Dtailwind.minify=false

# Force download of specific version
mvn tailwind:download -Dtailwind.version=v4.0.0 -Dtailwind.forceDownload=true
```

---

## 📁 Expected Directory Structure

```
your-project/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── resources/
│   │   │   └── static/
│   │   │       └── css/
│   │   │           └── input.css          ← Input file
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── Application.java
│   └── test/
└── target/
    └── classes/
        └── static/
            └── tailwind.css               ← Compiled file
```

---

## 🔧 Requirements

- **Java**: 17 or higher
- **Maven**: 3.6.0 or higher
- **Internet Connection**: To download the Tailwind binary (only the first time)

---

## 🌐 Supported Operating Systems

| OS | Architecture | Binary |
|----|----|---------|
| Windows | x86_64 | `tailwindcss-windows-x64.exe` |
| Windows | ARM64 | `tailwindcss-windows-arm64.exe` |
| macOS | x86_64 | `tailwindcss-macos-x64` |
| macOS | ARM64 | `tailwindcss-macos-arm64` |
| Linux | x86_64 | `tailwindcss-linux-x64` |
| Linux | ARM64 | `tailwindcss-linux-arm64` |
| Linux | ARMv7 | `tailwindcss-linux-armv7` |

---

## 📚 Common Use Cases

### 🍃 Use Case 1: Spring Boot Web Project

```xml
<plugins>
    <plugin>
        <groupId>com.deey</groupId>
        <artifactId>maven-tailwind-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <inputFile>${project.basedir}/src/main/resources/static/css/input.css</inputFile>
            <outputFile>${project.build.outputDirectory}/static/tailwind.css</outputFile>
            <minify>true</minify>
        </configuration>
    </plugin>
</plugins>
```

Then in your HTML/Thymeleaf/JTE:
```html
<!-- 🔑 IMPORTANT: Link to the compiled Tailwind CSS -->
<link rel="stylesheet" href="/tailwind.css">
```

### 💻 Use Case 2: Local Development

**Terminal 1: Compile and start the application**
```bash
mvn clean install
mvn spring-boot:run
```

**Terminal 2: Watch mode for CSS**
```bash
mvn tailwind:watch -Dtailwind.minify=false
```

### 🚀 Use Case 3: CI/CD Pipeline

```bash
# In your pipeline (GitHub Actions, GitLab CI, etc.)
mvn clean package
```

The plugin will automatically compile CSS during the `generate-resources` phase.

---

## 🐛 Troubleshooting

### ❌ Error: "Input file not found"

**Problem:** The plugin cannot find `input.css`

**Solution:**
1. Verify that the file exists at the configured path
2. Check that the path is correct in `pom.xml`
3. Create the file if it doesn't exist:

```bash
mkdir -p src/main/resources/static/css
echo "@tailwind base; @tailwind components; @tailwind utilities;" > src/main/resources/static/css/input.css
```

### ❌ Error: "Cannot create output directory"

**Problem:** The plugin cannot create the output directory

**Solution:**
1. Verify write permissions on the directory
2. Make sure the path doesn't contain special characters
3. Run Maven with elevated permissions if necessary

### ❌ Error: "Binary download failed"

**Problem:** Cannot download the Tailwind binary

**Solution:**
1. Check your internet connection
2. Verify that GitHub is accessible
3. Try with a different version:
   ```bash
   mvn tailwind:download -Dtailwind.version=v4.0.0
   ```
4. Try downloading again:
   ```bash
   mvn tailwind:download -Dtailwind.forceDownload=true
   ```

### ❌ Compiled CSS is empty

**Problem:** The generated CSS file contains no styles

**Solution:**
1. Verify that `input.css` imports `@import "tailwindcss";`
2. Check that you have Tailwind classes in your HTML templates
3. Note: In Tailwind CSS v4, `tailwind.config.js` is optional. The plugin supports v4.x by default

---

## 📝 Cache Files

Downloaded binaries are cached at:
```
~/.m2/tailwindcss/
```

To clear the cache and force download:
```bash
rm -rf ~/.m2/tailwindcss/
mvn tailwind:download -Dtailwind.forceDownload=true
```

---

## 🎯 Maven Lifecycle

The plugin automatically integrates with the Maven lifecycle:

- **initialize phase**: `mvn tailwind:download` (optional)
- **generate-resources phase**: `mvn tailwind:compile` (automatic)
- **manual**: `mvn tailwind:watch` (development)

To run only the compilation:
```bash
mvn generate-resources
```

---

## 📖 Tailwind Documentation

For more information about Tailwind CSS, visit:
- 📚 [Official Tailwind CSS Documentation](https://tailwindcss.com/docs)
- ⚙️ [Tailwind Configuration](https://tailwindcss.com/docs/configuration)
- 🛠️ [Tailwind CLI](https://tailwindcss.com/docs/installation)

---

## 👨‍💻 Author

**Andrei Cimpoeru**
- 📧 Email: andrei.web.app@gmail.com

---

## 📄 License

This project is available under the MIT license (or applicable license).

---

## ❓ Frequently Asked Questions (FAQ)

**Q: Do I need to have Tailwind installed globally?**
A: No, the plugin downloads it automatically.

**Q: Can I change the Tailwind version?**
A: Yes, use `-Dtailwind.version=v4.0.0` or configure it in `pom.xml`.

**Q: Does the plugin work on Windows?**
A: Yes, it works on Windows, macOS, and Linux.

**Q: What happens if I lose internet connection?**
A: The plugin will use the cached binary. If it doesn't exist, it will fail on the next compilation.

**Q: Can I use multiple CSS files?**
A: The plugin supports one input file and one output file. You can use `@import` within the CSS.

**Q: Is the compiled CSS optimized?**
A: Yes, by default it uses minification. You can disable it with `-Dtailwind.minify=false`.

---

## 📞 Support

If you encounter problems or have suggestions:
1. Check the "Troubleshooting" section
2. Consult the Tailwind CSS documentation
3. Open an issue on the project repository

---

**Last Updated**: February 2026

