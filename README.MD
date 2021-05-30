Для запуска необходимо:

- Создать базу данных postgresql
- Прописать реквизиты подключенияк этой БД 
в pom.xml, раздел propeties (db.url, db.user, db.password):
```
    <properties>
        <java.version>11</java.version>
        <db.url>jdbc:postgresql://localhost:5432/accountapp?characterEncoding=utf8</db.url>
        <db.user>accountapp</db.user>
        <db.password>accountapp</db.password>
    </properties>
```
- Выполнить mvn install (запустит миграции, которые создадут структуру БД)
- После этого на базе для создания тестовых данных можно выполнить скрипт init-test.sql, лежит в корне проекта
- После этого можно запускать приложение
- Файл, из которого берутся транзакции для процессинга ежедневно в 
00:00 по умолчанию должен находиться в папке пользователя:

  Для linux - /home/{user}/transactions.config
  Для windows - C:/Users/{user}/transactions.config
  
  Файл должен представлять из себя csv-файл, с 3 колонками: счёт списания, счёт зачисления и размер транзакции
  
  То есть:
  ```
  40814444444444444444,40813333333333333333,1.1
  40813333333333333333,40814444444444444444,2.2
  ...
  ```
  
  и т.д.
  
  В той же папке, в которой лежит transactions.config, при процессинге файла
  формируются отчёты с названием файла формата transactions{unix_time},
  для каждой транзакции (строчки) из transactions.config дописывается результат -
  SUCCESS или ERROR и идентификатор транзакции или описание ошибки.


После запуска можно вызвать эндпоинт:

POST http://localhost:8888/api/transactions
```
{
  "fromAccountNumber": "40814444444444444444",
  "toAccountNumber": "40813333333333333333",
  "amount": "1.1"
}
```

Успешный ответ:
```
{
    "transactionId": "f429f378d5f24cdbb008a21b1693b137"
}
```

При возникновении ошибок вернётся JSON вида:
```
{
    "timestamp": "2021-05-30T21:25:27.158+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Not enough funds for transaction on account - 40814444444444444444",
    "path": "/api/transactions"
}
```

При возникновении ошибок валидации запроса также 
возвращается поле details с массивом ошибок валидации:
```
{
    "path": "/api/transactions",
    "timestamp": "2021-05-30T17:53:06.125+00:00",
    "message": "Found 5 validation errors",
    "details":[
        {
            "message": "From account number length must be 20",
            "field": "fromAccountNumber",
            "rejectedValue": "awd"
        },
        {
            "message": "To account number length must be 20",
            "field": "toAccountNumber",
            "rejectedValue": "awd"
        },
        {
            "message": "From account number must contain only digits",
            "field": "fromAccountNumber",
            "rejectedValue": "awd"
        },
        {
            "message": "Number should have no more than 2 fractional numbers",
            "field": "amount",
            "rejectedValue": 1.111111
        },
        {
            "message": "To account number must contain only digits",
            "field": "toAccountNumber",
            "rejectedValue": "awd"
        }
    ],
    "error": "Bad request"
}
```

Также после запуска приложения доступен swagger-ui:

http://localhost:8888/swagger-ui.html
