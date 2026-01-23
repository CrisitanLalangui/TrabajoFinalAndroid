
from  django.contrib import  admin


from  Users.models import InfoPersonal


@admin.register(InfoPersonal)

class InfoPersonalAdmin(admin.ModelAdmin):
    list_display = ("user","document","phone","city","country")
    list_filter = ("city","country")
    search_fields = ("user__first_name","document","city__name")
    ordering = ("country", "city__name","user__first_name")