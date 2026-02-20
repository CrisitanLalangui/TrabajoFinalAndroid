import secrets

from django.db import models


class ArchivoModel(models.Model):
    name = models.CharField(max_length=45, null=False, blank=False, verbose_name="nombre")
    slug = models.SlugField(max_length=200, blank=True, null=True, unique=True)
    url = models.FileField(upload_to="archivos/")
    idTarjeta = models.CharField(max_length=6,blank=True,null=True)



    usuario = models.ForeignKey(
        'UserStudy',
        null=True,
        blank=True,
        on_delete=models.CASCADE,
        related_name='files'
    )

    class Meta:
        db_table = "archivos"
        verbose_name = "Archivo"
        verbose_name_plural = "Archivos"

    def __str__(self):
        return self.name

    def save(self, *args, **kwargs):
        if not self.slug:
            prov = secrets.token_hex(4)

            while ArchivoModel.objects.filter(slug=prov).exists():
                prov = secrets.token_hex(4)

            self.slug = prov

        super().save(*args, **kwargs)
