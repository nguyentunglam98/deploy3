package com.example.webDemo3.service.impl.assignRedStarServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.ClassRedStar;
import com.example.webDemo3.entity.ClassRedStarId;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.ClassRedStarRepository;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.assignRedStarService.CreateAssignRedStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CreateAssignRedStarServiceImpl implements CreateAssignRedStarService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRedStarRepository classRedStarRepository;

    private int[] indexClassOfRedStar;
    private List<Integer>[] indexRedStarOfClass;
    private int[][] flag;
    private Random ran = new Random();
    private static int n = 1000;
    int[][] population;
    int[][] populationFlag;
    int[] costValue;
    int size;
    int[] outputData;

    @Override
    public MessageDTO delete(Date fromDate) {
        MessageDTO message = new MessageDTO();
        Date dateCurrent = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(sdf.format(dateCurrent).equalsIgnoreCase(sdf.format(fromDate))
                || fromDate.before(dateCurrent)){
            message = Constant.NOT_DELETE_ASSIGN_REDSTAR;
            return message;
        }
        try {
            classRedStarRepository.deleteByFromDate(fromDate);
        } catch (Exception e) {
            System.out.println(e);
            message.setMessageCode(1);
            message.setMessage(e.toString());
        }
        message = Constant.SUCCESS;
        return message;
    }

    @Override
    public MessageDTO checkDate(Date fromDate) {
        MessageDTO message = new MessageDTO();
        message.setMessageCode(0);
        Date dateCurrent = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(sdf.format(dateCurrent).equalsIgnoreCase(sdf.format(fromDate))
                || fromDate.before(dateCurrent)){
            message = Constant.NOT_ADD_ASSIGN_REDSTAR;
            return message;
        }
        List<Date> date = classRedStarRepository.findByDate(fromDate);
        if (date != null && date.size() > 0) {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            message.setMessageCode(2);
            message.setMessage("Phân công sau ngày " + sdf.format(fromDate) + " sẽ bị xóa.\n Bạn có muốn tiếp tục ghi đè?");
        }
        return message;
    }

    @Transactional
    @Override
    public MessageDTO create(Date fromDate) {
        MessageDTO message = Constant.SUCCESS;
        Date dateCurrent = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(sdf.format(dateCurrent).equalsIgnoreCase(sdf.format(fromDate))
                || fromDate.before(dateCurrent)){
            message = Constant.NOT_ADD_ASSIGN_REDSTAR;
            return message;
        }
        try {
            // delete if fromdate exit
            MessageDTO messageCheckDate = checkDate(fromDate);
            if (messageCheckDate.getMessageCode() == 2) {
                classRedStarRepository.deleteByFromDate(fromDate);
            }
            List<Class> classList = classRepository.findAll();
            List<User> redStarList = userRepository.findRedStar();
            Date beforDate = classRedStarRepository.getBiggestClosetDate(fromDate);
            List<ClassRedStar> assignList = new ArrayList<>();
            User[] assignUser = new User[0];
            if (beforDate != null) {
                assignList = classRedStarRepository.findAllByDate(beforDate);
                assignUser = new User[assignList.size()];
                for (int k = 0; k < assignList.size(); k++) {
                    ClassRedStar data = assignList.get(k);
                    User userData = userRepository.findUserByUsername(data.getClassRedStarId().getRED_STAR());
                    assignUser[k] = userData;
                }
            }
            getIndex(classList, redStarList, assignList, assignUser);

            //genertic
            khoitao(redStarList.size());
//          khoitaoTest(redStarList.size());
//          laighep();
            while (true){
                danhgia(classList.size() * 2, redStarList.size());
                if (Print()) {
                    break;
                }
                chonloc();
                dotbien();
            }

            int l = danhgiaOne(classList.size() * 2, redStarList.size(), outputData);
            System.out.println(l);
//            outputData[outputData.length-1] =1000;
            insertClassRedStar(fromDate, classList, redStarList, outputData);

        } catch (Exception e) {
            System.out.println(e);
            message = new MessageDTO();
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    private void insertClassRedStar(Date fromDate,
                                          List<Class> classList, List<User> redStarList, int[] output) throws Exception {
        int i;
        try {
            for (i = 0; i < size; i++) {
                int indexClass = i;
                if (indexClass != 0) indexClass = indexClass / 2;
                Class classi = classList.get(indexClass);
                User redStar = redStarList.get(output[i]);
                ClassRedStar data = new ClassRedStar();
                ClassRedStarId dataID = new ClassRedStarId();
                dataID.setRED_STAR(redStar.getUsername());
                dataID.setFROM_DATE(fromDate);
                data.setClassRedStarId(dataID);
                data.setClassSchool(new Class(classi.getClassId()));
                classRedStarRepository.save(data);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new MyException(e.toString());
        }
    }

    private int[][] copyflag(int d,int c){
        int[][] flagcCopy = new int[d][c];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < c; j++) {
                int value = flag[i][j];
                flagcCopy[i][j] = value;
            }
        }
        return flagcCopy;
    }

    private void getIndex(List<Class> classList, List<User> redStarList,
                          List<ClassRedStar> assignList,User[] assignUser) {
        //set up
        size = classList.size() * 2;
        population = new int[n][size];
        populationFlag = new int[n][size];
        costValue = new int[n];
        indexClassOfRedStar = new int[redStarList.size()];
        indexRedStarOfClass = (List<Integer>[]) new List[classList.size()];
        flag = new int[classList.size() * 2][redStarList.size()];
        outputData = new int[size];

        for (int i = 0; i < classList.size(); i++) {
            Class classi = classList.get(i);
            indexRedStarOfClass[i] = new ArrayList<>();
            for (int j = 0; j < redStarList.size(); j++) {
                User redstar = redStarList.get(j);
                if (classi.getClassId() == redstar.getClassSchool().getClassId()) {
                    indexRedStarOfClass[i].add(j);
                    indexClassOfRedStar[j] = i;
                }
                //fill all flag = 0
                flag[i * 2][j] = 0;
                flag[i * 2 + 1][j] = 0;
            }
        }

        for (int i = 0; i < classList.size(); i++) {
            Class classi = classList.get(i);
            for (int j = 0; j < redStarList.size(); j++) {
                User redstar = redStarList.get(j);
                //loại bỏ cùng khối
                if (classi.getGrade() == redstar.getClassSchool().getGrade()) {
                    flag[i * 2][j] = -1;
                    flag[i * 2 + 1][j] = -1;
                }
                //loại bỏ chấm chéo 2 lần liên tiếp
                for (int k = 0; k < assignList.size(); k++) {
                    ClassRedStar data = assignList.get(k);
                    if ((data.getClassSchool().getClassId() == classi.getClassId())
                            && (data.getClassRedStarId().getRED_STAR() == redstar.getUsername())) {
                        flag[i * 2][j] = -1;
                        flag[i * 2 + 1][j] = -1;
                    }
                    User userData = assignUser[k];
                    //userRepository.findUserByUsername(data.getClassRedStarId().getRED_STAR());
                    if ((userData.getClassSchool().getClassId() == classi.getClassId())
                            && (data.getClassSchool().getClassId() == redstar.getClassSchool().getClassId())) {
                        flag[i * 2][j] = -1;
                        flag[i * 2 + 1][j] = -1;
                    }
                }
            }
        }
    }

    private void khoitao(int redStarListSize) {
        int[] data = new int[redStarListSize];
        for (int i = 0; i < redStarListSize; i++) {
            data[i] = i;
        }
        for (int i = 0; i < n; i++) {
            int[] copyData = data.clone();
            int max = redStarListSize;
            for (int j = 0; j < size; j++) {
                int value = ran.nextInt(max);
                population[i][j] = copyData[value];
                max--;
                copyData[value] = copyData[max];
            }
        }
    }

    private void danhgia(int d,int c) {
        for (int i = 0; i < n; i++) {
            costValue[i] = 0;
            int[][] flagCopy = copyflag(d,c);
            for (int classIndex = 0; classIndex < size; classIndex++) {
                int redStar = population[i][classIndex];
                if (flagCopy[classIndex][redStar] != 0) {
                    costValue[i]++;
                    populationFlag[i][classIndex] = -1;
                }
                //nếu data đúng
                else {
                    populationFlag[i][classIndex] = 0;
                    flagCopy[classIndex][redStar] = 1;
                    //2 sao đỏ cùng lớp không chấm cùng 1 lớp
                    if(classIndex%2 == 0){
                        flagCopy[classIndex+1][redStar] = 1;
                        int classOfRedstar = indexClassOfRedStar[redStar];
                        for (int redStarOfClass : indexRedStarOfClass[classOfRedstar]) {
                            flagCopy[classIndex + 1][redStarOfClass] = 1;
                        }
                    }
                    int k = 0;
                    if (classIndex != 0) k = classIndex/2;
                    //sao đỏ của 2 lớp không chấm chéo nhau
                    for (int redStarOfClass : indexRedStarOfClass[k]){
                        flagCopy[indexClassOfRedStar[redStar]*2][redStarOfClass] = 1;
                        flagCopy[indexClassOfRedStar[redStar]*2+1][redStarOfClass] = 1;
                    }
                }
            }
        }
    }

    public boolean Print() {
        int [] temp = costValue.clone();
        Arrays.sort(temp);
        int best = temp[0];
        System.out.print(best+": ");
        for (int i=0;i<n;i++){
            if (costValue[i]==best){
                for (int j=0;j<size;j++)
                    System.out.print(population[i][j]+" ,");
                System.out.println();
                if(best == 0){
                    outputData = population[i];
                }
                break;
            }
        }
        if(best == 0) {
            return true;
        }
        else return false;
    }

    public void chonloc() {
        int [] temp = costValue.clone();
        Arrays.sort(temp);
        int nguong = temp[n*50/100];
        for (int i=0;i<n;i++){
            if (costValue[i]>nguong){
                population[i]=population[ran.nextInt(n)].clone();
            }
        }
    }

    public void dotbien() {
        for (int i=0;i<n/2;i++){
            int cha=ran.nextInt(n);

            for (int j=0;j<size;j++)
                if(populationFlag[cha][j] == -1){
                    int value = ran.nextInt(size);
                    int temp=population[cha][j];
                    population[cha][j] = population[cha][value];
                    population[cha][value] = temp;
                }
        }
    }

    private int[] test(int redStarListSize, int[][] flagCopy) {
        int[] data = new int[redStarListSize];
        for (int i = 0; i < redStarListSize; i++) {
            data[i] = i;
        }
        int[] output = new int[size];
        for (int j = 0; j < size; j++) output[j] = -1;
        int maxfirst = redStarListSize;
        int kq = 1;
        for (int classIndex = 0; classIndex < size; classIndex++) {
            int t = 0;
            int[] copyData = data.clone();
            int max = maxfirst;
            if(classIndex == 82){
                int h=0;
            }
            while (true){
                int value ;
                if(max < 0){
                    if (output[classIndex] == -1)
                        kq =0;
                    break;
                }
                else if( max == 0){
                    value =0;
                }
                else {
                    value = ran.nextInt(max);
                }
                int redStar = copyData[value];
                if (flagCopy[classIndex][redStar] ==0) {
                    output[classIndex] = redStar;
                    flagCopy[classIndex][redStar] = 1;
                    if(classIndex%2 == 0) {
                        flagCopy[classIndex + 1][redStar] = 1;
                        int classOfRedstar = indexClassOfRedStar[redStar];
                        for (int redStarOfClass : indexRedStarOfClass[classOfRedstar]) {
                            flagCopy[classIndex + 1][redStarOfClass] = 1;
                        }
                    }
                    int k = 0;
                    if (classIndex != 0) k = classIndex/2;
                    for (int redStarOfClass : indexRedStarOfClass[k]){
                        flagCopy[indexClassOfRedStar[redStar]*2][redStarOfClass] = 1;
                        flagCopy[indexClassOfRedStar[redStar]*2+1][redStarOfClass] = 1;
                    }
                    if(maxfirst > 0){
                        for(int j =0;j<maxfirst;j++){

                            if(data[j] == redStar){
                                maxfirst--;
                                int temp = data[j];
                                data[j] = data[maxfirst];
                                data[maxfirst] = temp;
                                break;
                            }
                        }
                    }
                    else{
                        kq=2;
                        maxfirst--;
                    }
                    break;
                }
                else{
                    max--;
                    if(max>=0){
                        int temp = copyData[value];
                        copyData[value] = copyData[max];
                        copyData[max] = temp;
                    }
                }
            }
        }

        System.out.print("ket qua: "+kq+": ");
        for (int j=0;j<size;j++) {
            System.out.print(output[j] + " ,");
        }
        System.out.println();
        if (kq == 0){
            output[size -1] = -1;
        }
        return output;
    }

    private int[] craeteOnePopulation(int redStarListSize, int[][] flagCopy) {
        int[] data = new int[redStarListSize];
        for (int i = 0; i < redStarListSize; i++) {
            data[i] = i;
        }
        int[] output = new int[size];
        for (int j = 0; j < size; j++) output[j] = -1;
        int maxfirst = redStarListSize;
        int kq = 1;
        for (int classIndex = 0; classIndex < size; classIndex++) {
            int t = 0;
            int[] copyData = data.clone();
            int max = maxfirst;
            while (true){
                int value = 0;
                if(max < 0){
                    if (output[classIndex] == -1){
                        if(maxfirst > 0 ) value = ran.nextInt(maxfirst);
                        output[classIndex] = copyData[value];
                        if(maxfirst > 0){
                            for(int j =0;j<maxfirst;j++){
                                if(data[j] == copyData[value]){
                                    maxfirst--;
                                    int temp = data[j];
                                    data[j] = data[maxfirst];
                                    data[maxfirst] = temp;
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
                else if( max > 0){
                    value = ran.nextInt(max);
                }
                int redStar = copyData[value];
                // if data corect
                if (flagCopy[classIndex][redStar] ==0) {
                    output[classIndex] = redStar;
                    flagCopy[classIndex][redStar] = 1;
                    //2 sao đỏ cùng lớp không chấm cùng 1 lớp
                    if(classIndex%2 == 0) {
                        flagCopy[classIndex + 1][redStar] = 1;
                        int classOfRedstar = indexClassOfRedStar[redStar];
                        for (int redStarOfClass : indexRedStarOfClass[classOfRedstar]) {
                            flagCopy[classIndex + 1][redStarOfClass] = 1;
                        }
                    }
                    //sao đỏ của 2 lớp không chấm chéo nhau
                    int k = 0;
                    if (classIndex != 0) k = classIndex/2;
                    for (int redStarOfClass : indexRedStarOfClass[k]){
                        flagCopy[indexClassOfRedStar[redStar]*2][redStarOfClass] = 1;
                        flagCopy[indexClassOfRedStar[redStar]*2+1][redStarOfClass] = 1;
                    }
                    // đưa data đã chọn về cuối để không chọn lại
                    if(maxfirst > 0){
                        for(int j =0;j<maxfirst;j++){
                            if(data[j] == redStar){
                                maxfirst--;
                                int temp = data[j];
                                data[j] = data[maxfirst];
                                data[maxfirst] = temp;
                                break;
                            }
                        }
                    }
                    else{
                        kq=2;
                        maxfirst--;
                    }
                    break;
                }
                else{
                    // đưa data đã chọn về cuối để không chọn lại
                    max--;
                    if(max>=0){
                        int temp = copyData[value];
                        copyData[value] = copyData[max];
                        copyData[max] = temp;
                    }
                }
            }
        }

        System.out.print("ket qua: "+kq+": ");
        for (int j=0;j<size;j++) {
            System.out.print(output[j] + " ,");
        }
        System.out.println();
        if (kq != 1){
            output[size -1] = -1;
        }
        return output;
    }

    private void khoitaoTest(int redStarListSize) {
        int[] data = new int[redStarListSize];
        for (int i = 0; i < redStarListSize; i++) {
            data[i] = i;
        }
        for (int i = 0; i < n; i++) {
            //population[i] = new int[size];
            int[][] flagCopy = copyflag(size, redStarListSize);
            int[] output = craeteOnePopulation(redStarListSize, flagCopy);
            population[i] = output;
        }
    }

    public void laighep() {
        for (int i=0;i<n/2;i++){
            int cha=ran.nextInt(n);
            int me = ran.nextInt(n);

            int[] indexCha = new int[size];
            int[] indexme = new int[size];
            for (int j=0;j<size;j++){
                indexCha[population[cha][j]] = j;
                indexme[population[me][j]] = j;
            }

            for (int j=0;j<size;j++)
                if (ran.nextInt(2)==1){
               //if(populationFlag[cha][j] == -1){
                    int temp=population[cha][j];
                    population[cha][j]=population[me][j];
                    population[me][j]=temp;

                    temp = population[cha][indexCha[population[cha][j]]];
                    population[cha][indexCha[population[cha][j]]]
                            = population[me][indexCha[population[me][j]]];
                    population[me][indexCha[population[me][j]]] = temp;
                }
        }
    }

    private int danhgiaOne(int d,int c, int[] data){
        int kq = 0;
        int[] dd = new int[c];
        for(int i=0 ;i<c;i++){
            dd[i] = 0;
        }
        int[][] flagCopy = copyflag(d,c);
        for (int classIndex = 0; classIndex < size; classIndex++) {
            int redStar = data[classIndex];
            dd[redStar] ++;
            if(dd[redStar] > 1){
                kq ++;
            }
            if (flagCopy[classIndex][redStar] != 0) {
                kq ++;
            }
            else {
                flagCopy[classIndex][redStar] = 1;
                if (classIndex % 2 == 0) {
                    flagCopy[classIndex + 1][redStar] = 1;
                    int classOfRedstar = indexClassOfRedStar[redStar];
                    for (int redStarOfClass : indexRedStarOfClass[classOfRedstar]) {
                        flagCopy[classIndex + 1][redStarOfClass] = 1;
                    }
                }
                int k = 0;
                if (classIndex != 0) k = classIndex / 2;
                for (int redStarOfClass : indexRedStarOfClass[k]) {
                    flagCopy[indexClassOfRedStar[redStar] * 2][redStarOfClass] = 1;
                    flagCopy[indexClassOfRedStar[redStar] * 2 + 1][redStarOfClass] = 1;
                }
            }
        }
        return kq;
    }

}
//random with condition
//        int kq = 0;
//        int[] output = null;
//        for (int i = 0; i < 10000; i++) {
//            int[][] flagCopy = copyflag(classList.size() * 2, redStarList.size());
//            output = test(redStarList.size(), flagCopy);
//            if (output != null && output[size - 1] != -1) {
//                System.out.println(i);
//                break;
//            }
//        }
//
//        message = insertClassRedStar(message,fromDate,classList,redStarList,output);