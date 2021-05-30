from django.core.mail import send_mail
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.conf import settings
from . import vaccino
import sys

@api_view(["POST"])
def sendmail(request):
    print(request.data['email'])
    #json_val = vaccino.elaborate()
    json_val = "Test del dio cane"
    subject = 'Prenotazione Vaccino'
    message = "<h1>Messaggio</h1>"
    to_email = [request.data['email']]
    from_email = settings.EMAIL_HOST_USER
    if to_email:
        try:
            send_mail(subject, '', from_email, to_email, html_message=message)
        except:
            print(sys,sys.exc_info()[0])
            return Response(status=406)
        return Response(status=200, data=json_val)
    return Response(status=400)
