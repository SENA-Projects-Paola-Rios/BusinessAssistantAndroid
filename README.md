# 📱 Business Assistant Supermarket - Android

Esta aplicación móvil está diseñada como parte del desarrollo de un sistema de gestión de inventarios y ventas en supermercados.  
En esta versión se implementa únicamente el **login**, el **menú de navegación lateral** y la **vista de usuarios con su CRUD**, utilizando **Android Studio** y **Java**.

---

## 🚀 Características

- **Login de usuarios**: Inicio de sesión simulado con soporte para modo claro y oscuro.  
- **Pantalla de bienvenida**: Incluye menú lateral con navegación a diferentes secciones.  
- **Gestión de usuarios**: Visualización de usuarios, agregar, editar, eliminar y ver detalles mediante modales.  
- **Interacción**: Alternancia entre modo claro y oscuro, y navegación entre actividades.  

**Probado en:**
- Emulador Android Studio (Phone API 36).  
- Dispositivo físico conectado por USB.  

---

## 📂 Estructura del Proyecto

- **MainActivity.java** → Lógica del login y aplicación del tema claro/oscuro.  
- **WelcomeActivity.java** → Pantalla de bienvenida con menú lateral y toggle de tema.  
- **UsersActivity.java** → Vista de usuarios y CRUD completo.  
- **network/** → Comunicación con la API (AuthApi, UserApi, RetrofitClient, LoginRequest/Response).  
- **users/** → Modelos y adaptadores relacionados con usuarios (User, UserRepository, UsersAdapter).  

---

## ⚙ Configuración Técnica

- **Namespace**: `com.sena.businessassistantandroid`  
- **Application ID**: `com.sena.businessassistantandroid`  
- **Compile SDK**: 35  
- **Target SDK**: 35  
- **Min SDK**: 24  
- **Versión de la app**: 1.0  
- **Lenguaje**: Java  
- **Versión Java**: 11  
- **Test Runner**: `androidx.test.runner.AndroidJUnitRunner`  

## 🛠 Requisitos  

Antes de ejecutar el proyecto, asegúrate de contar con:  

- **Android Studio** (versión 2022.3 o superior recomendada).  
- **Java JDK 11** o superior.  
- **SDK de Android** instalado (mínimo API 24).  
- Un **emulador configurado** o un **dispositivo Android físico** con depuración USB habilitada.  

---

## 📥 Instalación y Ejecución  

### 1. Clonar el repositorio  
```bash
git clone https://github.com/SENA-Projects-Paola-Rios/BusinessAssistantAndroid.git
### Abrir el proyecto en Android Studio  
Selecciona **File > Open** y elige la carpeta del proyecto.  

### 2. Configurar el dispositivo de prueba  
- Usar un **emulador** desde **AVD Manager**.  
- O conectar un **smartphone físico** con **depuración USB** activada.  

### 3. Ejecutar la aplicación  
1. Haz clic en el botón **Run ▶** en Android Studio.  
2. Espera a que la app se instale y se ejecute.  

---

## 📄 Licencia  
Este proyecto es de **uso educativo** y **libre** para su consulta o modificación.  
