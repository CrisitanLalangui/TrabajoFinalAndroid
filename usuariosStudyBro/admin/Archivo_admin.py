
from django.contrib import admin
from usuariosStudyBro.models import ArchivoModel




@admin.register(ArchivoModel)
class ArchivoAdmnin(admin.ModelAdmin):
    list_display = ("name", "slug", "url","usuario","nombreTarjeta")
    search_fields = ("name",)
    ordering = ("-name",)










