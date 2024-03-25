package SotonHKPASS.Website_testing.services;

import SotonHKPASS.Website_testing.entity.Stock_market;

public interface Stock_service {
    public abstract void createStock(Stock_market stock);

    public abstract void updateStock(String id, Stock_market stock);

    public abstract void deleteStocks(String id);
    // public abstract Collection<Product> getProducts();

}