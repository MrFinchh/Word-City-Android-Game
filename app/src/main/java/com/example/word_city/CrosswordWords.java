package com.example.word_city;

public interface CrosswordWords
{
    String L1G1 = "Çam – Maç";
    String L1G2 = "Aşk – Kaş – Şak";
    String L1G3 = "Tas – Sat – Ast";
    String L1G4 = "Cam – Amca – Ama";
    String L1G5 = "Ser – Süre – Üre";
    String L1G6 = "Rüya – Yar – Ray";

    String [] L1_KEYS = {"L1G1","L1G2","L1G3","L1G4","L1G5","L1G6"};
    String [] L1_VALUES = {L1G1,L1G2,L1G3,L1G4,L1G5,L1G6};

    /******************************************************************************/

    String L2G1 = "ÇAYCI – ÇAY – ACI – AYI – AÇI";
    String L2G2 = "BEYAZ – YAZ – BAY – BEY – BEZ – BAZ";
    String L2G3 = "HAVUÇ – AVUÇ – HAÇ – VAH – AHU";
    String L2G4 = "BEKÇİ – KEÇİ – BEK – ÇEK – ÇEKİ";
    String L2G5 = "JETON – OJE – TON – NET – JET – NOT – TEN";
    String L2G6 = "KÖPEK – PEK – KÖK – KEP – KEK";

    String [] L2_KEYS = {"L2G1","L2G2","L2G3","L2G4","L2G5","L2G6"};
    String [] L2_VALUES = {L2G1,L2G2,L2G3,L2G4,L2G5,L2G6};

    /******************************************************************************/


    String L3G1 = "KOPÇA – PAK – ÇAP – ÇOK – OPAK – KOÇ – KAP – KAÇ";
    String L3G2 = "ÇANTA – TAÇ -ÇAN – ANAÇ – ANT – NAAT – ÇAT – ATA – ANA – TAN – ATAÇ";
    //String L3G2 = "KEŞİF – ŞEF – KEŞ – FİŞEK – EKŞİ – EŞİK – ŞEFİK – FİŞ – ŞİKE";
    //String L3G2 = " BALIK – AKIL – BAL – BAK – KIL – KAL – BAKI";
    String L3G3 = " KÜLAH – HALK – KÜL – KAL – AKÜ – HAK – HAL – KAH";
    String L3G4 = "MAYIN – AYNI – NAM – AYI – YAN – ANI";
    String L3G5 = "ÖLÇÜT – ÇÖL – ÖLÇÜ – ÖLÜ – TÜL";
    String L3G6 = "MERAK – KAR – KEM – KARE – ARK – MERA – KREMA – KAMER – KREM";

    String [] L3_KEYS = {"L3G1","L3G2","L3G3","L3G4","L3G5","L3G6"};
    String [] L3_VALUES = {L3G1,L3G2,L3G3,L3G4,L3G5,L3G6};

    /******************************************************************************/


    String [][] GAME_KEYS = {L1_KEYS,L2_KEYS,L3_KEYS};
    String [][] GAME_VALUES = {L1_VALUES,L2_VALUES,L3_VALUES};

}
