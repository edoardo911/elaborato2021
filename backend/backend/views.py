from django.core.mail import EmailMessage
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.conf import settings
from . import vaccino
import json
import traceback

@api_view(["POST"])
def sendmail(request):
    json_val = vaccino.elaborate(json.dumps(request.data))
    subject = 'Prenotazione Vaccino'
    message = "<b>Gentile cittadino,</b><br>La ringraziamo per aver scelto il servizio di prenotazione vaccini del comune di Agrigento. In allegato troverà un resoconto della sua prenotaione:"
    to_email = [request.data['email']]
    from_email = settings.EMAIL_HOST_USER
    if to_email:
        try:
            mail = EmailMessage(subject, message, from_email, to_email)
            mail.content_subtype = "html"
            mail.attach_file("/home/vacc/backend/backend/pdf/prenotazione-vaccino.pdf", "application/pdf")
            mail.send()
        except:
            traceback.print_exc()
            return Response(status=406)
        return Response(status=200, data=json_val)
    return Response(status=400)
