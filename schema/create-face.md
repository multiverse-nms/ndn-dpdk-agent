### Create Face


#### Method:  `Face.Create`

#### Parameters:

- Local: String indicating the local address
- Port: String indicating the port
- Remote: String indicating the remote address
- Scheme: String indicating the face scheme (e.g. ether, tcp, udp)

#### Json Schema:

```json
{
    "$schema": "http://json-schema.org/draft-07/schema",
    "$id": "http://example.com/example.json",
    "type": "object",
    "title": "The root schema",
    "description": "The root schema comprises the entire JSON document.",
    "default": {},
    "examples": [
        {
            "method": "Face.Create",
            "params": {
                "Local": "01:00:00:00:04:01",
                "Scheme": "ether",
                "Port": "0000:00:0a.0",
                "Remote": "02:00:00:00:04:02"
            }
        }
    ],
    "required": [
        "method",
        "params"
    ],
    "additionalProperties": true,
    "properties": {
        "method": {
            "$id": "#/properties/method",
            "type": "string",
            "title": "The method schema",
            "description": "An explanation about the purpose of this instance.",
            "default": "",
            "examples": [
                "Face.Create"
            ]
        },
        "params": {
            "$id": "#/properties/params",
            "type": "object",
            "title": "The params schema",
            "description": "An explanation about the purpose of this instance.",
            "default": {},
            "examples": [
                {
                    "Local": "01:00:00:00:04:01",
                    "Scheme": "ether",
                    "Port": "0000:00:0a.0",
                    "Remote": "02:00:00:00:04:02"
                }
            ],
            "required": [
                "Local",
                "Scheme",
                "Port",
                "Remote"
            ],
            "additionalProperties": true,
            "properties": {
                "Local": {
                    "$id": "#/properties/params/properties/Local",
                    "type": "string",
                    "title": "The Local schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "01:00:00:00:04:01"
                    ]
                },
                "Scheme": {
                    "$id": "#/properties/params/properties/Scheme",
                    "type": "string",
                    "title": "The Scheme schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "ether"
                    ]
                },
                "Port": {
                    "$id": "#/properties/params/properties/Port",
                    "type": "string",
                    "title": "The Port schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "0000:00:0a.0"
                    ]
                },
                "Remote": {
                    "$id": "#/properties/params/properties/Remote",
                    "type": "string",
                    "title": "The Remote schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "02:00:00:00:04:02"
                    ]
                }
            }
        }
    }
}
```


#### Example

```json
{
  "method" : "Face.Create",
  "id" : "1984c5e1-e947-4a76-afd4-1555cbe1991d",
  "params" : {
    "Local": "01:00:00:00:04:01",
    "Scheme" : "ether",
    "Port" : "0000:00:0a.0",
    "Remote" : "02:00:00:00:04:02"
  },
  "jsonrpc" : "2.0"
}
```
