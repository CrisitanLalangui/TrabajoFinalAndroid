from django.contrib import admin  # Importa la funcionalidad de administración de Django
from Users.models import CiudadModel  # Importa el modelo CiudadModel que creamos


# Clase para personalizar cómo se muestra CiudadModel en el admin
class CiudadAdmin(admin.ModelAdmin):
    # ----------------------------
    # Columnas que se mostrarán en la lista de registros en el admin
    # ----------------------------
    list_display = ("name", "slug")
    # Muestra el nombre y el slug en la tabla principal del admin

    # ----------------------------
    # Campos por los que se puede buscar en la barra de búsqueda del admin
    # ----------------------------
    search_fields = ("name",)
    # Permite buscar ciudades por su nombre

    # ----------------------------
    # Orden por defecto de la lista de registros
    # ----------------------------
    ordering = ("-name",)
    # Ordena las ciudades de forma descendente por nombre

    # ----------------------------
    # Campos que se muestran como solo lectura en el formulario de edición
    # ----------------------------
    readonly_fields = ("slug",)
    # El slug se genera automáticamente, por eso no se puede editar desde el admin

# Registrar el modelo junto con la clase de configuración personalizada
admin.site.register(CiudadModel, CiudadAdmin)
# Esto indica que Django debe usar CiudadAdmin para gestionar el admin de CiudadModel
# Esto equivale a decir a Django, oye muestrame esta conmiguracion en el panel de Jango
