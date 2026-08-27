# WebExcercise

Project Servlet/JSP/JPA Hibernate mau theo mo hinh:

- `entity`: khai bao bang `Category`, `Video`
- `dao`: thao tac DB bang JPA
- `service`: xu ly nghiep vu
- `controller`: Servlet controller
- `view`: JSP trong `src/main/webapp/views`
- `config`: cau hinh JPA, upload, lifecycle

## Database

Mac dinh ung dung dung SQL Server:

```powershell
-Dapp.database=sqlserver
```

Dung MySQL:

```powershell
-Dapp.database=mysql
```

Co the cau hinh bang bien moi truong:

```powershell
$env:APP_DATABASE="mysql"
$env:APP_DB_USER="root"
$env:APP_DB_PASSWORD="mat_khau_mysql_cua_ban"
```

Hoac SQL Server:

```powershell
$env:APP_DATABASE="sqlserver"
$env:APP_DB_USER="sa"
$env:APP_DB_PASSWORD="mat_khau_sql_server_cua_ban"
```

Persistence units:

- `jpa-hibernate-sqlserver`: database `jakartaJPA`
- `jpa-hibernate-mysql`: database `servletjpa`

Script mau:

- `database/sqlserver-sample.sql`
- `database/mysql-sample.sql`

Hibernate dang bat:

```xml
<property name="hibernate.hbm2ddl.auto" value="update"/>
```

Nen khi database da ton tai va user co quyen, Hibernate co the tu tao/cap nhat bang theo entity.

## URL

Sau khi deploy len Tomcat 10+ hoac chay Jetty:

- `/admin/categories`
- `/admin/category/add`
- `/admin/videos`
- `/admin/video/add`

Thu muc upload anh:

```text
%USERPROFILE%\WebExcerciseUploads
```
