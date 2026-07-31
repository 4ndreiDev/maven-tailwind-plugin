# Maven Tailwind Plugin for Spring Boot - Tailwind CSS v4

## 📑 Table of Contents

- [What is the Maven Tailwind Plugin?](#what-is-the-maven-tailwind-plugin)
  - [Why Use This Plugin?](#why-use-this-plugin)
  - [Who Should Use This?](#who-should-use-this)
- [⚡ Quick Start Guide](#-quick-start-guide)
- [📋 Available Maven Commands](#-available-maven-commands)
- [⚙️ Configuration Parameters](#️-configuration-parameters)
- [📁 Expected Directory Structure](#-expected-directory-structure)
- [🔧 Requirements](#-requirements)
- [🌐 Supported Operating Systems](#-supported-operating-systems)
- [🐛 Troubleshooting](#-troubleshooting)
- [📝 Cache Files](#-cache-files)
- [🎯 Maven Lifecycle](#-maven-lifecycle)
- [📖 Tailwind Documentation](#-tailwind-documentation)
- [📚 Additional Documentation](#-additional-documentation)
- [👨‍💻 Author](#-author)
- [📄 License](#-license)
- [❓ Frequently Asked Questions (FAQ)](#-frequently-asked-questions-faq)
- [📞 Support](#-support)

---

## What is the Maven Tailwind Plugin?

The **Maven Tailwind Plugin** is a powerful Maven plugin that simplifies the integration of **Tailwind CSS v4** into your Maven-based Java projects, including **Spring Boot** applications. Instead of managing Node.js dependencies or dealing with npm packages, this plugin provides seamless Tailwind CSS compilation directly through Maven commands.

Built and maintained by **4ndreiDev**, this plugin brings the modern utility-first CSS approach of Tailwind directly into the Maven ecosystem.

### Why Use This Plugin?

- 🚫 **Zero Node.js Dependency**: No need to install Node.js or npm in your project. The plugin automatically downloads the Tailwind CLI binary compatible with your operating system.
- 🔗 **Seamless Maven Integration**: The plugin integrates naturally with Maven's build lifecycle. Your CSS is compiled automatically during the build process without extra configuration.
- ⚡ **Fast Development Workflow**: Use watch mode to get instant CSS recompilation as you modify your templates and add Tailwind classes.
- 📦 **Production-Ready**: Automatic CSS minification for production builds, reducing file size and improving load times.
- 🌍 **Cross-Platform Support**: Works on Windows, macOS, and Linux with support for multiple architectures (x64, ARM64, ARMv7).
- 🔄 **Version Control**: Easily switch between different Tailwind CSS versions without modifying your local environment.

### Who Should Use This?

This plugin is ideal for:
- 🏗️ Maven-based projects (Spring Boot, Jakarta EE, etc.) looking to integrate Tailwind CSS v4
- 👥 Teams that prefer Maven over npm/yarn for dependency management
- 🎯 Projects where you want a single build tool (Maven) instead of multiple tools
- 🔐 Organizations with strict security policies that prefer not to use npm
- ⚡ Developers who want a streamlined, battle-tested solution (crafted by 4ndreiDev)

---


## ⚡ Quick Start Guide

Here's a complete walkthrough of setting up Tailwind CSS in a new Spring Boot project:

### Step 1: Add the plugin to your pom.xml

Open your `pom.xml` and add the plugin to the `<build>` section:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.4ndreidev</groupId>
            <artifactId>tailwind-maven-plugin</artifactId>
            <version>1.2.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Step 2: Initialize Tailwind CSS
```bash
mvn tailwind:init
```

This creates:
- Directory: `src/main/resources/static/css/`
- File: `input.css` with Tailwind CSS v4 imports

### Step 3: Create your HTML template with Tailwind classes
Create `src/main/resources/templates/index.html`:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Tailwind CSS v4 | Maven Tailwind Plugin by 4ndreiDev -->
    <link rel="stylesheet" href="/tailwind.css">
    <title>Tailwind App</title>
</head>
<body>
    <div class="flex h-screen items-center justify-center bg-gradient-to-r from-blue-500 to-purple-600">
        <div class="text-center">
            <h1 class="text-5xl font-bold text-white mb-4">Hello, Tailwind CSS v4!</h1>
            <p class="text-lg text-blue-100">Powered by Maven Tailwind Plugin • Created by 4ndreiDev</p>
        </div>
    </div>
</body>
</html>
```

### Step 4: Run your application
```bash
mvn spring-boot:run
```

Visit `http://localhost:8080` and see your styled page!

### Step 5: Development workflow
In a separate terminal, run watch mode:
```bash
mvn tailwind:watch
```

Now whenever you modify your HTML or add new Tailwind classes, the CSS will automatically recompile.

---

## 📋 Available Maven Commands

### Command 1: Initialize Project (One-time Setup)
```bash
mvn tailwind:init
```

**What it does:**
- Creates the CSS input directory structure
- Generates `src/main/resources/static/css/input.css`
- Adds Tailwind CSS v4 imports automatically
- Safe to run multiple times (skips if file exists)

**Use case:** Use this command once when setting up your project.

### Command 2: Compile CSS
```bash
mvn tailwind:compile
```

**What it does:**
- Compiles the CSS input file
- Generates `target/classes/static/tailwind.css` (minified by default)
- Automatically executes in the `generate-resources` phase during builds
- Can be run manually anytime

**Use case:** Use this to manually trigger compilation or as part of your build process.

### Command 3: Watch Mode (Development)
```bash
mvn tailwind:watch
```

**What it does:**
- Monitors changes in your CSS/HTML files
- Automatically recompiles when changes are detected
- CSS is NOT minified by default in watch mode
- Press `Ctrl+C` to stop

**Minify in watch mode (optional):**
```bash
mvn tailwind:watch -Dtailwind.minify=true
```

**Use case:** Run this in a separate terminal during development for instant feedback.

### Command 4: Download Binary (Advanced)
```bash
mvn tailwind:download
```

**What it does:**
- Downloads the Tailwind CLI binary for your OS
- Automatically executes in the `initialize` phase
- Useful for preloading the cache

**Force download (ignoring cache):**
```bash
mvn tailwind:download -Dtailwind.forceDownload=true
```

**Use case:** Use this if you need to update the cached binary.

---

## ⚙️ Configuration Parameters

Customize the plugin behavior using properties in your `pom.xml` or from the command line.

| Parameter | Maven Property | Default Value | Description |
|-----------|-----------------|-------------------|-------------|
| `inputFile` | `tailwind.inputFile` | `${project.basedir}/src/main/resources/static/css/input.css` | Path to the CSS input file |
| `outputFile` | `tailwind.outputFile` | `${project.build.outputDirectory}/static/tailwind.css` | Path to the compiled CSS file |
| `minify` | `tailwind.minify` | `true` (compile), `false` (watch) | Minify CSS in compilation |
| `version` | `tailwind.version` | `v4.3.3` | Version of Tailwind CSS to use. See [available versions](https://github.com/tailwindlabs/tailwindcss/releases/) |
| `downloadDirectory` | `tailwind.downloadDirectory` | `${user.home}/.m2/tailwindcss` | Directory where the binary is cached |
| `baseUrl` | `tailwind.baseUrl` | `https://github.com/tailwindlabs/tailwindcss/releases/download` | Base URL for downloading the binary |
| `forceDownload` | `tailwind.forceDownload` | `false` | Force binary download (ignore cache) |

### Configuration Examples

**Example 1: Customize in `pom.xml`**

```xml
<plugin>
    <groupId>io.github.4ndreidev</groupId>
    <artifactId>tailwind-maven-plugin</artifactId>
    <version>1.2.0</version>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <inputFile>${project.basedir}/src/main/resources/css/styles.css</inputFile>
        <outputFile>${project.build.outputDirectory}/css/output.css</outputFile>
        <minify>true</minify>
        <version>v4.3.3</version>
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
| macOS | x86_64 | `tailwindcss-macos-x64` |
| macOS | ARM64 | `tailwindcss-macos-arm64` |
| Linux | x86_64 | `tailwindcss-linux-x64` |
| Linux | ARM64 | `tailwindcss-linux-arm64` |

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

Downloaded binaries are cached per Tailwind version at:
```
~/.m2/tailwindcss/<version>/
```

For example, the Windows binary for Tailwind CSS v4.3.3 is stored at:
```
~/.m2/tailwindcss/v4.3.3/tailwindcss-windows-x64.exe
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

## 📚 Additional Documentation

- 📋 **[CHANGELOG.md](CHANGELOG.md)** - Version history and new features
- 🛠️ **[DEVELOPMENT.md](DEVELOPMENT.md)** - Guide for developers: How to add new Mojos and extend the plugin

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

**Q: Do I need to create the input.css file manually?**
A: No, use `mvn tailwind:init` to generate it automatically. It's safe to run multiple times (skips if the file already exists).

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

**Q: Do I need to run `tailwind:init` every time?**
A: No, `tailwind:init` is a one-time setup command. After that, use `tailwind:compile` or `tailwind:watch` for development.

---

## 📞 Support

If you encounter problems or have suggestions:
1. Check the "Troubleshooting" section
2. Consult the Tailwind CSS documentation
3. Open an issue on the project repository

---

**Maven Tailwind Plugin v1.2.0** | Built for Tailwind CSS v4 with ❤️ by **4ndreiDev**

**Last Updated**: July 2026

