package com.woohaengshi.backend.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowCalendarResult {

    private int date;
    private int time;
    private List<SubjectResult> subjects = new ArrayList<>();

    public static ShowCalendarResult init(int date){
        return new ShowCalendarResult(date, 0, new ArrayList<>());
    }
}
