// Online C compiler to run C program online
#include <stdio.h>
#include <stdlib.h>
struct item
{
    int code;
    int quantity;
};

struct item *space (int n)
{
    if (n <= 0){
        return NULL;
    }
    struct item *p = malloc(n * sizeof(struct item));
    return p;
}

int store (int pos, struct item *arr, struct item str)
{
    if (arr == NULL){
        return -1;
    }
    if (pos < 0){
        return -1;
    }

    (arr+pos)->code = str.code;
    (arr+pos)->quantity = str.quantity;
    return 0;
}

double average (struct item *arr, int m)
{   
    if(arr == NULL){
        return -1;
    }
    if (m <= 0){
        return -1;
    }
    double avg = 0;
    for (int i = 0 ; i< m; i++){
        if ((arr+i)->quantity < 0){
            return -1;
        }
        avg += (arr+i)->quantity;
    }
    avg/=m;
    return avg;
}

