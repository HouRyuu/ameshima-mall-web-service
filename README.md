# ameshima-mall-web-service
是非[ビデオ](https://www.youtube.com/watch?v=Ho-PMXgPNRY)をご覧ください
    プロジェクトはまだ未完成です。
#### スタート手順
##### 1. mysqlをスタート
    sudo /usr/local/mysql/support-files/mysql.server start 
##### 2. rabbitmqをスタート
    rabbitmq-server
##### 3. redisをスタート
    src/redis-server redis.conf
##### 4. elasticsearch-2.4.6をスタート
    ./bin/elasticsearch



#### 説明
##### 1．商品一覧
    HPで色んな商品を閲覧して、好みでおすすめできます。Elasticsearchで気楽に欲しい商品を検索できます。
##### 2．ユーザ登録
    普通の登録だけではなくて、更に外部アカウント(Alipay)でも登録可能
##### 3．ユーザ関連情報登録
    ユーザ情報、アドレス帳など。
##### 4．商品登録
    商品情報、写真、パラメタ、SKU、在庫などを登録し、顧客に閲覧られます。
##### 5．商品詳細
    お店に登録された商品情報を詳しく見えます。
##### 6．注文
    好きな商品をカートに入れ、または直接に注文できます。
