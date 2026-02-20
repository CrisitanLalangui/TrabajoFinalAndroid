# Importamos el módulo admin de Django
from django.contrib import admin

# Importamos UserAdmin, la clase base que nos permite personalizar la interfaz de administración
from django.contrib.auth.admin import UserAdmin

# Importamos nuestro modelo de usuario personalizado
from Users.models.users_model import User


# ===============================================================
# Creamos nuestra propia clase de administración para el modelo User
# ===============================================================
class CustomUserAdmin(UserAdmin):
    # -----------------------------------------------------------
    # Columnas que se mostrarán en la lista de usuarios en el admin
    # -----------------------------------------------------------
    list_display = ("email", "username", "role__name", "is_active", 'is_staff', "is_superuser")
    # - email, username → información básica del usuario
    # - role__name → nombre del rol asignado (relación ForeignKey)
    # - is_active, is_staff, is_superuser → campos de estado y permisos

    # -----------------------------------------------------------
    # Filtros laterales para la lista de usuarios
    # -----------------------------------------------------------
    list_filter = ("is_active", 'is_superuser')
    # Permite filtrar fácilmente usuarios activos/inactivos y superusuarios en el admin

    # -----------------------------------------------------------
    # Campos por los que se puede buscar en el admin
    # -----------------------------------------------------------
    search_fields = ("email", "first_name")
    # Permite buscar usuarios por correo o nombre

    # -----------------------------------------------------------
    # Orden por defecto en la lista de usuarios
    # -----------------------------------------------------------
    ordering = ("-email", 'is_superuser')
    # Primero ordena por email descendente, luego prioriza superusuarios

    # -----------------------------------------------------------
    # Configuración de los campos que se muestran al editar un usuario
    # -----------------------------------------------------------
    fieldsets = (
        ("Inicio de sesión", {  # Título de la sección
            "classes": ("wide",),  # Clase CSS para la interfaz
            "fields": ("email", "password"),  # Campos que aparecerán
        }),
        ("Información personal", {
            "classes": ("wide",),
            "fields": (
                "username",
                "first_name",
                "last_name",
                "is_active",
                "is_staff",
                "is_superuser",
                "role"  # Incluye el rol asignado
            ),
        }),
    )

    # -----------------------------------------------------------
    # Configuración de los campos al **crear un nuevo usuario** desde el admin
    # -----------------------------------------------------------
    add_fieldsets = (
        ("Inicio de sesión", {
            "classes": ("wide",),#estilo de clase de Jango, la ventana
            "fields": ("email", "password1", "password2", "role"),
            # password1 y password2 se usan para confirmar la contraseña
        }),
        ("Información personal", {
            "classes": ("collapse",),  # Esta sección se muestra colapsada por defecto, con la flechita hacia abajo
            "fields": (
                "username",
                "first_name",
                "last_name",
                "is_active",
                "is_staff",
                "is_superuser"
            ),
        }),
    )


# =============================================================
# Registramos el modelo User junto con nuestra clase CustomUserAdmin
# ===============================================================
admin.site.register(User, CustomUserAdmin)
# Esto indica a Django que use CustomUserAdmin para gestionar la interfaz del modelo User
