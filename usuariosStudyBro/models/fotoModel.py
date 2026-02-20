import secrets

from django.db import models


class FotoModel(models.Model):
    name = models.CharField(max_length=45, null=False, blank=False, verbose_name="nombre")
    slug = models.SlugField(max_length=200, blank=True, null=True, unique=True)
    url = models.ImageField(upload_to="imagenes/")

    usuario = models.ForeignKey(
        'UserStudy',
        null=True,
        blank=True,
        on_delete=models.CASCADE,
        related_name='files'
    )

    class Meta:
        db_table = "fotos"
        verbose_name = "Foto"
        verbose_name_plural = "Fotos"
    def __str__(self):
        return self.name

    def save(self, *args, **kwargs):
        if not self.slug:
            prov = secrets.token_hex(4)
            while FotoModel.objects.filter(slug=prov).exists():
                prov = secrets.token_hex(4)

            self.slug = prov

        super().save(*args, **kwargs)







