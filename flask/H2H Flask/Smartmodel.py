from currency_converter import CurrencyConverter
import pickle 
import numpy as np
import pandas as pd


def predict(df):
    
    df = pd.DataFrame(df)
    
    #print("Count of order where OCD>RDD",(df.shape[0]-df[df['ORDER_CREATION_DATE']<=df['REQUESTED_DELIVERY_DATE']].shape[0]))
    #print("% of order where OCD>RDD",print("% order data removed ",((df[df['ORDER_CREATION_DATE']>df['REQUESTED_DELIVERY_DATE']].shape[0])/df.shape[0])*100))



    df['ORDER_CREATION_DATE'] = df['ORDER_CREATION_DATE'].str.replace('-', '')
    df['REQUESTED_DELIVERY_DATE'] = df['REQUESTED_DELIVERY_DATE'].str.replace('-', '')
    df['ORDER_CREATION_DATE']=pd.to_datetime(df['ORDER_CREATION_DATE'],format="%Y%m%d",errors='coerce')
    df['REQUESTED_DELIVERY_DATE']=pd.to_datetime(df['REQUESTED_DELIVERY_DATE'],format="%Y%m%d",errors='coerce')


    c=CurrencyConverter(fallback_on_missing_rate=True,fallback_on_wrong_date=True)
    df['amount_in_usd']=df[~(df['ORDER_CURRENCY'].isin(['HU1','AED','BHD','OMR','KWD','SAR','QAR']))].apply( lambda x: c.convert( x['ORDER_AMOUNT'],x['ORDER_CURRENCY'], 'USD',x['ORDER_CREATION_DATE']), axis = 1)
    #df['amount_in_usd']



    # currency for which there is no lookback data...use manual conversion.

    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='HU1',(df['ORDER_AMOUNT']*0.0026),df['amount_in_usd'])
    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='AED',(df['ORDER_AMOUNT']*0.27),df['amount_in_usd'])
    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='BHD',(df['ORDER_AMOUNT']*2.65),df['amount_in_usd'])
    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='OMR',(df['ORDER_AMOUNT']*2.60),df['amount_in_usd'])
    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='KWD',(df['ORDER_AMOUNT']*3.26),df['amount_in_usd'])
    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='SAR',(df['ORDER_AMOUNT']*0.27),df['amount_in_usd'])
    df['amount_in_usd']=np.where(df['ORDER_CURRENCY']=='QAR',(df['ORDER_AMOUNT']*0.27),df['amount_in_usd'])

    df['amount_in_usd'].sort_values(ascending=False)/1000000
    #print(df['amount_in_usd'])







    #df[['CUSTOMER_NUMBER','COMPANY_CODE']].head()


    df['unique_cust_id']=df['CUSTOMER_NUMBER'].astype(str)+df['COMPANY_CODE'].astype(str)
    #df['unique_cust_id'].nunique()


    #print(df['unique_cust_id'])


    def removeOutlier(group, net_amount_col):
        mean, std = np.mean(group[net_amount_col]), np.std(group[net_amount_col])
        group[net_amount_col] = np.where(group[net_amount_col]>mean+3*std, mean, group[net_amount_col])
        return group

    df_amount_removed_3 = df.groupby('unique_cust_id').apply(lambda x: removeOutlier(x, 'amount_in_usd'))
    #sns.set(rc={'figure.figsize':(20,5)})
    #sns.boxplot(x=df_amount_removed_3.ORDER_CREATION_DATE.dt.year,y=df_amount_removed_3.amount_in_usd)



    #print("FOR 3")
    #print("% of inv removed",((df.shape[0]-df_amount_removed_3.shape[0])/df.shape[0])*100)
    #print("% of amount removed ",((df['amount_in_usd'].sum()-df_amount_removed_3['amount_in_usd'].sum())/df['amount_in_usd'].sum())*100)


    df_amount_removed=df_amount_removed_3.copy()
    del df_amount_removed_3


    #print(df_amount_removed)




    import lightgbm as lgb
    from sklearn.metrics import r2_score,mean_squared_error,mean_absolute_error,mean_squared_error
    from sklearn.feature_selection import RFE

    list_of_cols = ["COMPANY_CODE", "CUSTOMER_NUMBER", "amount_in_usd", "ORDER_CREATION_DATE", "ORDER_CURRENCY"]
    customer_id_col = 'unique_cust_id'
    net_amount_col = 'amount_in_usd'
    create_date_col = 'ORDER_CREATION_DATE'

    #print("*"*100)

    #Aggregating order data on daily level

    #print("before Aggregating order data on daily level", df_amount_removed.shape)

    #model_1 = df_amount_removed.groupby([customer_id_col,create_date_col])[net_amount_col].sum().reset_index()
    model_1 = df_amount_removed[["unique_cust_id","ORDER_CREATION_DATE","amount_in_usd"]]

    #print("after Aggregating order data on daily level", model_1.shape)

    #print(model_1)



    def create_more_lags(melt, lags, ffday, customer_id_col, create_date_col, net_amount_col):
        for i in range(ffday, lags+1):
            melt['Last-'+str(i)+'day_Sales'] = melt.groupby([customer_id_col])[net_amount_col].shift(i)

        melt = melt.reset_index(drop = True)

        for i in range(ffday, lags+1):
            melt['Last-'+str(i)+'day_Diff']  = melt.groupby([customer_id_col])['Last-'+str(i)+'day_Sales'].diff()
        melt = melt.fillna(0)
        return melt


    def add_datepart(df, fldname, drop):
        fld = df[fldname]
        if not np.issubdtype(fld.dtype, np.datetime64):
            df[fldname] = fld = pd.to_datetime(fld, 
                                         infer_datetime_format=True)
        for n in ('Year', 'Month', 'Week', 'Day', 'Dayofweek', 
                'Dayofyear', 'Is_month_end', 'Is_month_start', 
                'Is_quarter_end', 'Is_quarter_start', 'Is_year_end', 
                'Is_year_start'):
            df[n] = getattr(fld.dt,n.lower())

        if drop: df.drop(fldname, axis=1, inplace=True)

        return df

    model_1 = create_more_lags(model_1,60, 1, customer_id_col, create_date_col, net_amount_col)

    # date feat like is_year,is_month etc
    model_1 = add_datepart(model_1, create_date_col, False)

    #print(model_1)

    features = model_1.drop([net_amount_col,create_date_col,customer_id_col], axis = 1).columns

    model = pickle.load(open("finalized_model.sav", 'rb'))

    prediction = np.expm1(model.predict(model_1[features]))

    return prediction