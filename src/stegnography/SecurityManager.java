package stegnography;

class SecurityManager {
    private String password;
    private int index;
    private int len;
    SecurityManager(String password)
    {
        index = 0;
        this.len = password.length();
        this.password = password;
    }
    int getPermutation()
    {
        int sum = 0;
        for(int i = 0; i < len; i++)
            sum += (int)password.charAt(i);
        return sum % 3 + 1;
    }
    int primaryCrypto(int x)
    {
        int y = x ^ (int)password.charAt(index);
        index = (index + 1) % len;
        return y;
    }
}
