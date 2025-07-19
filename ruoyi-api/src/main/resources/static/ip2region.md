# ip2region文件生成
- 1 下载项目https://gitee.com/lionsoul/ip2region
- 2 编辑java文件
  ```java
  cd make/java
  生成dbMaker-{version}.java
  cd ${ip2region_root}/java/
  java -jar dbMaker-1.2.2.jar -src ./data/ip.merge.txt -region ./data/global_region.csv
```
- 3 生成ip2region.db文件
- 4 替换db文件