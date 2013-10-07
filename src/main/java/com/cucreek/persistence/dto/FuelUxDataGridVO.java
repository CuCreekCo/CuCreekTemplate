package com.cucreek.persistence.dto;

/**
 * This class provides a JSON wrapper for the Fuel UX datagrid JavaScript component data source.  See:
 * http://exacttarget.github.com/fuelux/#datagrid
 *
 * @author jljdavidson
 */

public class FuelUxDataGridVO extends BasePersistenceDTO {
   private static final long serialVersionUID = 1L;
   private Object data;
   private int start;
   private int end;
   private int count;
   private int pages;
   private int page;
   private int perPage;

   public void setPerPage(int perPage) {
      this.perPage = perPage;
   }

   public int getPerPage() {
      return this.perPage;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getPage() {
      return this.page;
   }

   public void setPages(int pages) {
      this.pages = pages;
   }

   public int getPages() {
      return this.pages;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getCount() {
      return this.count;
   }

   public void setEnd(int end) {
      this.end = end;
   }

   public int getEnd() {
      return this.end;
   }

   public void setStart(int start) {
      this.start = start;
   }

   public int getStart() {
      return this.start;
   }

   public void setData(Object data) {
      this.data = data;
   }

   public Object getData() {
      return this.data;
   }
}
