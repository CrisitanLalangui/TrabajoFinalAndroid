
from django.contrib import admin
from django.contrib.admin import ModelAdmin

from Productos.models import Categoria


@admin.register(Categoria)
class CategoriaAdmin(ModelAdmin):
    list_display = ("nombre","slug")
    list_filter = ("nombre",)
    search_fields = ("nombre",)
    list_per_page = 25