from django.contrib import admin
from usuariosStudyBro.models import UserStudy
from usuariosStudyBro.models import ArchivoModel


class ArchivoInline(admin.StackedInline):  # o StackedInline
    model = ArchivoModel
    can_delete = False
    verbose_name_plural = "archivos"
    verbose_name = "archivo"

@admin.register(UserStudy)
class UserAdmin(admin.ModelAdmin):
    list_display = ('email',)
    search_fields = ('email',)
    ordering = ('email',)
    fields = ('email', 'password')
    inlines = [ArchivoInline]