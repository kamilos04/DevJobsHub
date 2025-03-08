import { differenceInCalendarDays, differenceInHours, differenceInSeconds, parse } from "date-fns";

export const formatExpirationDate = (date: Date, time: string): string => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    
    return `${day}-${month}-${year} ${time}:59`;
};

export const parseDateTime = (dateTimeStr: string): { date: Date; time: string } => {
    const [datePart, timePart] = dateTimeStr.split(" ");
    const [day, month, year] = datePart.split("-").map(Number);
    
    const date = new Date(year, month - 1, day);
  
    return { date, time: timePart.slice(0,-3) };
  };


export const parseStringToDate = (dateString: string) => {
    return parse(dateString, 'dd-MM-yyyy HH:mm:ss', new Date());

}

export const calcDaysToExpirationDateFromString = (dateString: string) => {
    const parsedDate = parseStringToDate(dateString)
    const today = new Date();
    return differenceInCalendarDays(parsedDate, today)

}

export const calcSecondsToExpirationDateFromString = (dateString: string) => {
    const parsedDate = parseStringToDate(dateString)
    const today = new Date();
    return differenceInSeconds(parsedDate, today)
}

export const calcHoursToExpirationDateFromString = (dateString: string) => {
    const parsedDate = parseStringToDate(dateString)
    const today = new Date();
    return differenceInHours(parsedDate, today)
}
