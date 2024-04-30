package com.example.quiettimeapp.TimeTable;

public class WeekScheduleClass
{
    private int FirstPeriod;
    private int SecondPeriod;
    private int ThirdPeriod;
    private int FourthPeriod;
    private int FifthPeriod;
    private int SixthPeriod;
    private int SeventhPeriod;
    private int EightPeriod;
    private int NinethPeriod;

    public WeekScheduleClass() {
    }

    public WeekScheduleClass(int firstPeriod, int secondPeriod, int thirdPeriod, int fourthPeriod, int fifthPeriod, int sixthPeriod, int seventhPeriod, int eightPeriod, int ninethPeriod)
    {
        FirstPeriod = firstPeriod;
        SecondPeriod = secondPeriod;
        ThirdPeriod = thirdPeriod;
        FourthPeriod = fourthPeriod;
        FifthPeriod = fifthPeriod;
        SixthPeriod = sixthPeriod;
        SeventhPeriod = seventhPeriod;
        EightPeriod = eightPeriod;
        NinethPeriod = ninethPeriod;
    }

    @Override
    public String toString()
    {
        return "WeekScheduleClass{" +
                "FirstPeriod=" + FirstPeriod +
                ", SecondPeriod=" + SecondPeriod +
                ", ThirdPeriod=" + ThirdPeriod +
                ", FourthPeriod=" + FourthPeriod +
                ", FifthPeriod=" + FifthPeriod +
                ", SixthPeriod=" + SixthPeriod +
                ", SeventhPeriod=" + SeventhPeriod +
                ", EightPeriod=" + EightPeriod +
                ", NinethPeriod=" + NinethPeriod +
                '}';
    }

    public int getFirstPeriod() {
        return FirstPeriod;
    }

    public void setFirstPeriod(int firstPeriod) {
        FirstPeriod = firstPeriod;
    }

    public int getSecondPeriod() {
        return SecondPeriod;
    }

    public void setSecondPeriod(int secondPeriod) {
        SecondPeriod = secondPeriod;
    }

    public int getThirdPeriod() {
        return ThirdPeriod;
    }

    public void setThirdPeriod(int thirdPeriod) {
        ThirdPeriod = thirdPeriod;
    }

    public int getFourthPeriod() {
        return FourthPeriod;
    }

    public void setFourthPeriod(int fourthPeriod) {
        FourthPeriod = fourthPeriod;
    }

    public int getFifthPeriod() {
        return FifthPeriod;
    }

    public void setFifthPeriod(int fifthPeriod) {
        FifthPeriod = fifthPeriod;
    }

    public int getSixthPeriod() {
        return SixthPeriod;
    }

    public void setSixthPeriod(int sixthPeriod) {
        SixthPeriod = sixthPeriod;
    }

    public int getSeventhPeriod() {
        return SeventhPeriod;
    }

    public void setSeventhPeriod(int seventhPeriod) {
        SeventhPeriod = seventhPeriod;
    }

    public int getEightPeriod() {
        return EightPeriod;
    }

    public void setEightPeriod(int eightPeriod) {
        EightPeriod = eightPeriod;
    }

    public int getNinethPeriod() {
        return NinethPeriod;
    }

    public void setNinethPeriod(int ninethPeriod) {
        NinethPeriod = ninethPeriod;
    }
}


