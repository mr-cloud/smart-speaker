package uni.mlgb.onlyapp.shit.service.model;

import java.util.Objects;
import java.util.Set;

/**
 * Created by zhangxin516 on 5/11/18
 */
public class Player {
    String uname;
    Set<String> aliases;

    public Player(String chineseName) {
        uname = chineseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getUname(), player.getUname());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUname());
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }
}
