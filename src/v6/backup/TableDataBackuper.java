package v6.backup;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;


final class TableDataBackuper extends RequestBackuper{
  
  private String table;
  
  public TableDataBackuper(DbBackuper b, String url, String table){
    super(b, url);
    this.table = table;
  }
    
  protected void error(){
    System.out.println("(!) Tabulka "+table+": Nepodarilo se zalohovat data tabulky!");
  }
  
  protected void success(){
    System.out.println("(+) Tabulka "+table+": Data tabulky uspesne zalohovana.");
  }
  
  protected void start(){
    System.out.println("(i) Tabulka "+table+": Zalohuji data tabulky.");
  }
  
  
  protected String getUrlSuffix(){
    String r;
    try{
      r = "?action=data&table="+URLEncoder.encode(table, "utf-8");
    }catch(UnsupportedEncodingException e){
      r = "error";
    };
    return r;
  }
  
  protected String getFileName(){
    return table+".data.tbl";
  }
  
}
