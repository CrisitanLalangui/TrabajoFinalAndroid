from django.db import  models
from django.utils.text import slugify


class Categoria(models.Model):
    nombre = models.CharField(max_length=200, blank=False,null=False)
    slug = models.SlugField(max_length=200,blank=True,null=True, unique=True)


    class Meta:

        db_table  = 'categories'
        verbose_name = 'Categoría'
        verbose_name_plural = 'Categorías'
        ordering = ['nombre']

    def __str__(self):
       return self.nombre

    def save(self, *args, **kwargs):
        if not self.slug:
            base = slugify((self.nombre))
            cont = 1
            prov = base

            while Categoria.objects.filter(slug=prov).exists():
                prov = base + "-"+ str(cont)
            self.slug = prov

        super().save(*args,**kwargs)