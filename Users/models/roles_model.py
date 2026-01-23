import secrets

from django.db import models

#1 -> Crear la clase

#2 -> Crear los modelos, o sea los roles, con sus condiciones
class Role(models.Model):
    name = models.CharField(max_length=50,unique=True, null=False,blank=False, verbose_name="Tipo rol")
    slug = models.SlugField(max_length=50,unique=True,null=False,blank=False) #Slug -> identificado de letras,

    is_active = models.BooleanField(default=True)

    class Meta:
        db_table = 'roles'
        verbose_name = 'Rol de usuario'
        verbose_name_plural = 'Roles de usuarios'
        ordering = ('name',)

    def save(self,*args,**kwargs):
        if not self.slug:
            prov = secrets.token_hex(8)
            while Role.objects.filter(slug=prov).exists():
                prov = secrets.token_hex(8)

            self.slug = prov

        super().save(*args,**kwargs)