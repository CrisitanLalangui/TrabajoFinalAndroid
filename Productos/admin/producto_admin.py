
from django.contrib import  admin
from django.contrib.admin import ModelAdmin

from Productos.models import Producto

@admin.register(Producto)
class ProductoAdmin(ModelAdmin):
    list_display = ("nombre","precio","categoria__nombre")
    list_filter = ("categoria__nombre",)
    search_fields = ("nombre","categoria__nombre")

