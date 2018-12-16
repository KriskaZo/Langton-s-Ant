public class Configuration {
    private String [] configuration;

    public Configuration (String configuration) {
        int length = configuration.length();
        this.configuration = new String [length];

        for (int i = 0; i < length; i++){
          if (i != length-1) {
              this.configuration [i] = configuration.substring(i, i+1);
          } else {
              this.configuration[i] = configuration.substring(i);
          }
        }
    }

    public String [] getConfiguration () {
        return configuration;
    }
}
