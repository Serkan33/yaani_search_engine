# Yaani Search Engine
Yaani Search Engine,  spring boot teknolojisini kullanan bir rest api uygulamasıdır. Uygulama sitemap.xml adreslerinden ``` <loc> ``` tagına ait tüm değerleri alır PostgreSQL veritabanına kaydeder. Yaani Search Engine docker kullanılarak container teknolojisiyle geliştirilmiştir. Proje asenkron  olarak geliştirilmiştir. 

### Localde Çalıştırmak 
  1. src/main/resources klasorünün altında bulunan application.properties dosyasında veritabanı bilgileri girilir.
  2. SearchengineApplication.java classı çalıştırılarak uygulama başlatılır.
  3. Uyglama 8080 portunda ayağa kalkmış olacaktır.
  
### Docker İle Çalıştırmak 
  1. Docker Desktop uygulaması çalıştırılır.
  2. Terminal içerisinden projenin root dizinine gelinir. 
  3. ``` docker compose -f docker-compose-infra.yml up ``` komutu çalıştırılarak PostgreSQL container ayağa kaldırılır.
  4. PostgreSQL container ayağa kalktıktan sonra  ``` mvn clean install ``` komutu çalıştırılır.
  5. Build success olduktan sonra yeni bir terminal penceresinde  ``` docker compose up --build ``` komutu çalıştırılır.
  6. Uyglama 8080 portunda ayağa kalkmış olacaktır.
 
Proje heroku üzerinde de host edilmektedir. Api dokümanına [Yaani Search API Swagger](https://yaani.herokuapp.com/swagger-ui/index.html) adresinden erişebilirsiniz.

 ### Uygulama Testi
  POST request ile /parse/xml endpointine body içerisinde geçerli bir sitemap.xml url adresi verilir.
  
  ```
  curl -X 'POST' 'https://yaani.herokuapp.com/parse/xml'
  -H 'accept: */*' \
  -H 'Content-Type: application/json'
  -d '{"sitemapUrl": "https://www.haberturk.com/sitemap_headlines.xml"}'
  ```
  Xmlde parse edilen url sayısı ve işlem süresini görüntülemek için GET request ile  /stats endpointini GET çağırabilirsiniz. 
  
   ```
   curl -X 'GET' 'https://yaani.herokuapp.com/stats' -H 'accept: */*'
  ```

