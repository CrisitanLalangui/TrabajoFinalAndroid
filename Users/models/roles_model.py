import secrets
# Librería estándar de Python.
# Se usa para generar valores aleatorios seguros (en este caso para el slug).

from django.db import models
# Importamos models para poder crear modelos de Django (tablas de base de datos).


# Modelo que representa un Rol de usuario
# Hereda de models.Model, lo que le da acceso a métodos como save()
class Role(models.Model):

    # Nombre del rol

    name = models.CharField(
        max_length=50,
        unique=True,
        null=False,
        blank=False,
        verbose_name="Tipo rol"
    )

    # Slug del rol
    # SlugField → texto corto, ideal para URLs
    # unique=True → debe ser único en la base de datos
    # null=False / blank=False → obligatorio
    # Se genera automáticamente si no se proporciona
    slug = models.SlugField(
        max_length=50,
        unique=True,
        null=True,
        blank=True
    )

    # Indica si el rol está activo o no
    # Por defecto, el rol se crea activo
    is_active = models.BooleanField(default=True)


    # Configuración adicional del modelo
    class Meta:#Nombre de la tabla y sus tipos de ordenes
        # Nombre real de la tabla en la base de datos
        db_table = 'roles'

        # Nombre en singular que se muestra en el admin
        verbose_name = 'Rol de usuario'

        # Nombre en plural que se muestra en el admin
        verbose_name_plural = 'Roles de usuarios'

        # Orden por defecto cuando se consultan los roles
        ordering = ('name',)


    # Método save()
    # Este método se ejecuta cada vez que se guarda el objeto
    # (role.save(), objects.create(), admin, etc.)
    def save(self, *args, **kwargs):

        # self es el OBJETO ACTUAL que se está guardando
        # Ejemplo: role = Role(name="Admin")
        #          role.save()
        #          → self == role

        # Si el slug todavía NO existe (es None o vacío)
        if not self.slug:
            # Genera un valor hexadecimal aleatorio
            # 8 bytes → 16 caracteres
            # Ejemplo: "a3f9c7e41b2d8a90"
            prov = secrets.token_hex(8)

            # Verifica que el slug no exista ya en la base de datos
            # Si existe, genera uno nuevo
            while Role.objects.filter(slug=prov).exists():
                prov = secrets.token_hex(8)

            # Asigna el slug generado al objeto actual (self)
            # Aún NO se guarda en la base de datos
            self.slug = prov

        # Llama al método save() original de Django (models.Model.save)
        # Aquí es donde realmente ocurre:
        # - INSERT si el objeto es nuevo
        # - UPDATE si el objeto ya existe
        super().save(*args, **kwargs)
