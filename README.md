**Introduction**
query basics info and the newest dynamical info of NBA players through interacting with smart speaker.

### Demo
- static info
    curl -X POST -H "Content-Type: application/json" -d '{"version":"1.0","session":{"isNew":true,"sessionId":"1","user":null,"device":null,"application":{"applicationId":"jd.alpha.skill.777f8f8192864764b54e40eca3fb808e"},"contexts":{}},"request":{"requestId":null,"type":"IntentRequest","timestamp":null,"dialogState":null,"intent":{"name":"static_info","confirmResult":null,"slots":{"star":{"name":null,"value":"凯里欧文","matched":false,"confirmResult":null},"sport":{"name":null,"value":"篮球","matched":false,"confirmResult":null}}}}}' localhost:8084/sample
- dynamic info
    curl -X POST -H "Content-Type: application/json" -d '{"version":"1.0","session":{"isNew":true,"sessionId":"1","user":null,"device":null,"application":{"applicationId":"jd.alpha.skill.777f8f8192864764b54e40eca3fb808e"},"contexts":{}},"request":{"requestId":null,"type":"IntentRequest","timestamp":null,"dialogState":null,"intent":{"name":"dynamic_info","confirmResult":null,"slots":{"star":{"name":null,"value":"勒布朗詹姆斯","matched":false,"confirmResult":null},"sport":{"name":null,"value":"篮球","matched":false,"confirmResult":null}}}}}' localhost:8084/sample
