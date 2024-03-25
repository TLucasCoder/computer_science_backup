package SotonHKPASS.Website_testing.entity;

import javax.persistence.*;

@Entity
@Table(name = "team_quantity")
public class Team_quantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money", nullable = false)
    private Long money;

    @Column(name = "labour_level", nullable = false)
    private Long labour_level;

    @Column(name = "salary", nullable = false)
    private Long salary;

    @Column(name = "stress", nullable = false)
    private Double stress;

    public Team_quantity() {
    }

    public Team_quantity(Long money, Long labour_level, Long salary, Double stress) {
        this.money = money;
        this.labour_level = labour_level;
        this.salary = salary;
        this.stress = stress;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getMoney() {
        return this.money;
    }

    public void setLabour_level(Long labour_level) {
        this.labour_level = labour_level;
    }

    public Long getLabour_level() {
        return this.labour_level;
    }

    public void setSalary(Long money) {
        this.money = money;
    }

    public Long getSalary() {
        return this.money;
    }

    public void setStress(Double stress) {
        this.stress = stress;
    }

    public Double getStress() {
        return this.stress;
    }

}