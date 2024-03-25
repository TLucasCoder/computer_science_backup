package SotonHKPASS.Website_testing.entity;

import javax.persistence.*;

@Entity
@Table(name = "stock_market")
public class Stock_market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_name", nullable = false)
    private Long stock_name;

    @Column(name = "stock_value", nullable = false)
    private Long stock_value;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setStock_name(Long stock_name) {
        this.stock_name = stock_name;
    }

    public Long getStock_name() {
        return this.stock_name;
    }

    public void setStock_value(Long stock_value) {
        this.stock_value = stock_value;
    }

    public Long getStock_value() {
        return this.stock_value;
    }
}
