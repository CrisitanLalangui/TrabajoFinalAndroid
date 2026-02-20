
from  django.contrib import admin
from Users.models import Role
class RolesAdmin(admin.ModelAdmin):



    @admin.register(Role)
    class RolesAdmin(admin.ModelAdmin):
        list_display = ("name","slug", "is_active", )
        list_filter = ("is_active",)

        fieldsets = (
        ("Información del rol", {
            "classes" : ("wide", ),
            "fields" : ("name","slug","is_active")
        }),
        )
