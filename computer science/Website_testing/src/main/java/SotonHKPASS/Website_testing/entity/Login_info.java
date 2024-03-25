package SotonHKPASS.Website_testing.entity;

import javax.persistence.*;

@Entity
@Table(name = "login_info")
public class Login_info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "acc_type", nullable = false)
    private String acc_type;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAcc(String acc_type) {
        this.acc_type = acc_type;
    }

    public String getAcc_type() {
        return this.acc_type;
    }
}
