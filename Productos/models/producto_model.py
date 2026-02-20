import secrets

from django.db import models


class Producto(models.Model):
    precio = models.DecimalField(max_digits=10, decimal_places=2,verbose_name="Precio",blank=False)
    nombre = models.CharField(max_length=200,blank=False,null=False,unique=True)
    slug = models.SlugField(max_length=200, blank=True, null=True,unique=True)
    descripcion = models.TextField(max_length=200,blank=True,null=True)


    #N:1 -> ForeginKey
    categoria = models.ForeignKey("Categoria",on_delete=models.PROTECT)#La categoría se puede borrar si hay un producto ligado a el,pero los productos no

    class Meta:
        db_table = 'productos'
        ordering = ['nombre']
        verbose_name = 'Producto'
        verbose_name_plural = 'Productos'

    def __init__(self, *args, **kwargs):
        super().__init__(args, kwargs)
        self.id = None

    def __str__(self):
        return  f"{self.nombre}  [{self.categoria.nombre}]"


    def save(self, *args,**kwargs):
        if not self.slug:
          prov = secrets.token_hex(8)
          while Producto.objects.filter(slug=prov).exists():
              prov = secrets.token_hex(8)
          self.slug = prov

        super().save(*args, **kwargs)