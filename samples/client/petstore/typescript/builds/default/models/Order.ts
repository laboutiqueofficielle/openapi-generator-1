/*
	TODO: LICENSE INFO
*/

/**
* An order for a pets from the pet store
*/
export class Order {
    'id'?: number;
    'petId'?: number;
    'quantity'?: number;
    'shipDate'?: Date;
    /**
    * Order Status
    */
    'status'?: OrderStatusEnum;
    'complete'?: boolean;

    static readonly discriminator: string | undefined = undefined;

    static readonly attributeTypeMap: Array<{name: string, baseName: string, type: string}> = [
        {
            "name": "id",
            "baseName": "id",
            "type": "number"
        },
        {
            "name": "petId",
            "baseName": "petId",
            "type": "number"
        },
        {
            "name": "quantity",
            "baseName": "quantity",
            "type": "number"
        },
        {
            "name": "shipDate",
            "baseName": "shipDate",
            "type": "Date"
        },
        {
            "name": "status",
            "baseName": "status",
            "type": "OrderStatusEnum"
        },
        {
            "name": "complete",
            "baseName": "complete",
            "type": "boolean"
        }    ];

    static getAttributeTypeMap() {
        return Order.attributeTypeMap;
    }
    
    public constructor() {
    }
}


export type OrderStatusEnum = "placed" | "approved" | "delivered" ;

