package itba.edu.ar;


import itba.edu.ar.api.Algorithm;
import itba.edu.ar.api.Encription;
import itba.edu.ar.api.Mode;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.util.Objects;

public class App {

    @Option(name = "-embed", usage = "Indica que se va a ocultar información", forbids = {"-extract"})
    private Boolean embed = false;

    @Option(name = "-extract", usage = "Indica que se va a extraer información", forbids = {"-embed"})
    private Boolean extract = true;

    @Option(name = "-steg", usage = "Algoritmo de esteganografiado: LSB de 1bit, LSB de 4 bits, LSB Improved", required = true)
    private Algorithm stegobmpAlgorithm;

    @Option(name = "-a", usage = "Algoritmo de encripción")
    private Encription encription = Encription.AES128;

    @Option(name = "-m ", usage = "Modo de encripción")
    private Mode mode = Mode.CBC;

    @Option(name = "-in", usage = "Archivo que se va a ocultar", depends = {"-embed"}, handler = StringArrayOptionHandler.class)
    private String[] inFilename = {Objects.requireNonNull(App.class.getClassLoader().getResource("config.properties")).getPath()};

    @Option(name = "-p", usage = "Archivo bmp que será el portador.", required = true, handler = StringArrayOptionHandler.class)
    private String[] holderFilename = {Objects.requireNonNull(App.class.getClassLoader().getResource("config.properties")).getPath()};

    @Option(name = "-out", usage = "Archivo bmp de salida, es decir, el archivo bitmapfile con la información de file incrustada.", required = true, handler = StringArrayOptionHandler.class)
    private String[] outFilename = {Objects.requireNonNull(App.class.getClassLoader().getResource("config.properties")).getPath()};

    @Option(name = "-pass", usage = "password de encripcion", handler = StringArrayOptionHandler.class)
    private String[] encriptionPassword = {Objects.requireNonNull(App.class.getClassLoader().getResource("config.properties")).getPath()};
    /*
        No se puede encriptar/desencriptar sin password. Si este dato no está, sólo se esteganografia.
    */

    public Boolean getEmbed() {
        return embed;
    }

    public Boolean getExtract() {
        return extract;
    }

    public Algorithm getStegobmpAlgorithm() {
        return stegobmpAlgorithm;
    }

    public Encription getEncription() {
        return encription;
    }

    public Mode getMode() {
        return mode;
    }

    public String getEncriptionPassword() {
        return extractString(encriptionPassword);
    }

    public String getInFilename() {
        return extractString(inFilename);
    }

    public String getHolderFilename() {
        return extractString(holderFilename);
    }

    public String getOutFilename() {
        return extractString(outFilename);
    }

    private String extractString(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (String s_ : s) {
            sb.append(s_.replaceAll("\\\\", ""));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        App app = new App();

        final CmdLineParser parser = new CmdLineParser(app);
        if (args.length < 1) {
            parser.printUsage(System.err);
            System.exit(1);
        }
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

    }
}
