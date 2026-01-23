

from django.contrib import admin

from Users.models import CiudadModel


class CiudadAdmin(admin.ModelAdmin):
    list_display = ("name","slug")
    search_fields = ("name",)
    ordering = ("-name",)
    readonly_fields = ("slug",)
admin.site.register(CiudadModel,CiudadAdmin)
