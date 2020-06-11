package itba.edu.ar;

import itba.edu.ar.api.SteganographyAlgorithm;
import itba.edu.ar.utils.criptography.EncriptionAlgorithm;
import itba.edu.ar.utils.criptography.EncriptionMode;
import itba.edu.ar.utils.criptography.Encriptor;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.util.Arrays;
import java.util.Optional;

public class App {

    @Option(name = "-embed", usage = "Indica que se va a ocultar información", forbids = {"-extract"})
    private Boolean embed;

    @Option(name = "-extract", usage = "Indica que se va a extraer información", forbids = {"-embed"})
    private Boolean extract;

    @Option(name = "-steg", usage = "Algoritmo de esteganografiado: LSB de 1bit, LSB de 4 bits, LSB Improved", required = true)
    private SteganographyAlgorithm stegoAlgorithm;

    @Option(name = "-a", usage = "Algoritmo de encripción")
    private EncriptionAlgorithm encriptionAlgorithm = EncriptionAlgorithm.AES128;

    @Option(name = "-m", usage = "Modo de encripción")
    private EncriptionMode encriptionMode = EncriptionMode.CBC;

    @Option(name = "-in", usage = "Archivo que se va a ocultar", depends = {"-embed"}, handler = StringArrayOptionHandler.class)
    private String[] inFilename = {}; // Se settea como un empty array por que el handler StringArrayOptionHandler.class no considera valores default null

    @Option(name = "-p", usage = "Archivo bmp que será el portador.", required = true, handler = StringArrayOptionHandler.class)
    private String[] holderFilename;

    @Option(name = "-out", usage = "Archivo bmp de salida, es decir, el archivo bitmapfile con la información de file incrustada.", required = true, handler = StringArrayOptionHandler.class)
    private String[] outFilename;

    @Option(name = "-pass", usage = "password de encripcion", handler = StringArrayOptionHandler.class)
    private String[] encriptionPassword = {};  // Se settea como un empty array por que el handler StringArrayOptionHandler.class no considera valores default null
    /*
        No se puede encriptar/desencriptar sin password. Si este dato no está, sólo se esteganografia.
    */

    public Optional<Boolean> getEmbed() {
        return Optional.ofNullable(embed);
    }

    public Optional<Boolean> getExtract() {
        return Optional.ofNullable(extract);
    }

    public Optional<SteganographyAlgorithm> getStegoAlgorithm() {
        return Optional.ofNullable(stegoAlgorithm);
    }

    public Optional<EncriptionAlgorithm> getEncriptionAlgorithm() {
        return Optional.ofNullable(encriptionAlgorithm);
    }

    public Optional<EncriptionMode> getEncriptionMode() {
        return Optional.ofNullable(encriptionMode);
    }

    public Optional<String> getEncriptionPassword() {
        return extractString(encriptionPassword);
    }

    public Optional<String> getInFilename() {
        return extractString(inFilename);
    }

    public Optional<String> getHolderFilename() {
        return extractString(holderFilename);
    }

    public Optional<String> getOutFilename() {
        return extractString(outFilename);
    }

    private Optional<String> extractString(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (String s_ : s) {
            sb.append(s_.replaceAll("\\\\", ""));
            sb.append(" ");
        }
        String value = sb.toString().trim();
        return value.isEmpty() ? Optional.empty() : Optional.of(value);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        App app = new App();

        final CmdLineParser parser = new CmdLineParser(app);
        if (args.length < 1) {
            parser.printUsage(System.err);
            System.exit(1);
        }
        try {
            parser.parseArgument(args);
            if (app.getEmbed().orElse(false) && app.embed && !app.getInFilename().isPresent()) {
                throw new CmdLineException("Option \"-in\" is required");
            }
            if (!app.getEmbed().isPresent() && !app.getExtract().isPresent()) {
                throw new CmdLineException("Either option \"-embed\" or \"-extract\" is required");
            }
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        Steganographer.SteganographerBuilder stegoBuilder = new Steganographer.SteganographerBuilder()
                .withLsb(app.getStegoAlgorithm().get().getEncryptor());

        if (app.getEncriptionPassword().isPresent()) {
            stegoBuilder.withCipher(Encriptor.from(app.getEncriptionAlgorithm().get(), app.getEncriptionMode().get()), app.getEncriptionPassword().get());
        }

        Steganographer stego = stegoBuilder.build();

        try {
            if (app.getEmbed().orElse(false)) {
                stego.embed(app.getInFilename().get(), app.getHolderFilename().get(), app.getOutFilename().get());
            } else if (app.getExtract().orElse(false)) {
                stego.extract(app.getHolderFilename().get(), app.getOutFilename().get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "App{" + "\n" +
                "embed=" + embed + "\n" +
                ", extract=" + extract + "\n" +
                ", stegobmpAlgorithm=" + stegoAlgorithm + "\n" +
                ", encription=" + encriptionAlgorithm + "\n" +
                ", mode=" + encriptionMode + "\n" +
                ", inFilename=" + Arrays.toString(inFilename) + "\n" +
                ", holderFilename=" + Arrays.toString(holderFilename) + "\n" +
                ", outFilename=" + Arrays.toString(outFilename) + "\n" +
                ", encriptionPassword=" + Arrays.toString(encriptionPassword) + "\n" +
                '}';
    }
}
