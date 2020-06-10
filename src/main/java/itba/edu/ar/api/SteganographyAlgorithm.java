package itba.edu.ar.api;

public enum SteganographyAlgorithm {
    LSB1(){
        @Override
        public LSB getEncryptor() {
            return null;
        }
    }, LSB4(){
        @Override
        public LSB getEncryptor() {
            return null;
        }
    }, LSBI(){
        @Override
        public LSB getEncryptor() {
            return null;
        }
    };

    public abstract LSB getEncryptor();
}
